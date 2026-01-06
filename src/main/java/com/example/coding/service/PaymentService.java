package com.example.coding.service;

import com.example.coding.config.UnlockWebSocketHandler;
import com.example.coding.entity.Payment;
import com.example.coding.mapper.PaymentMapper;
import com.example.coding.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 支付服务
 *
 * 注意：当前实现为模拟支付系统，用于演示和测试
 *
 * 生产环境集成指南：
 * 1. 申请微信支付商户号：https://pay.weixin.qq.com/
 * 2. 引入微信支付SDK：
 *    <dependency>
 *        <groupId>com.github.wechatpay-apiv3</groupId>
 *        <artifactId>wechatpay-java</artifactId>
 *        <version>0.2.12</version>
 *    </dependency>
 * 3. 配置商户号和API密钥
 * 4. 实现createPayment调用微信Native下单API
 * 5. 实现checkPaymentStatus调用微信查询订单API
 * 6. 配置支付回调接口接收微信通知
 */
@Service
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final UserMapper userMapper;
    private final UserService userService;
    private final UnlockWebSocketHandler webSocketHandler;

    // 模拟：存储支付状态（生产环境应从数据库查询）
    private final Map<String, String> paymentStatusCache = new HashMap<>();

    public PaymentService(PaymentMapper paymentMapper, UserMapper userMapper, UserService userService, UnlockWebSocketHandler webSocketHandler) {
        this.paymentMapper = paymentMapper;
        this.userMapper = userMapper;
        this.userService = userService;
        this.webSocketHandler = webSocketHandler;
    }

    /**
     * 创建支付订单
     * 生产环境：调用微信支付Native下单接口，返回二维码URL
     */
    public String createPayment(String userId, Integer amount, String content) {
        String paymentId = UUID.randomUUID().toString();

        Payment payment = new Payment()
                .setId(paymentId)
                .setUserId(userId)
                .setAmount(amount)
                .setContent(content)
                .setCreateTime(LocalDateTime.now())
                .setVerified(false);

        paymentMapper.insert(payment);

        // 初始化支付状态为等待支付
        paymentStatusCache.put(paymentId, "pending");

        // 生产环境：这里应该调用微信支付API创建订单
        // 返回二维码URL给前端显示
        // WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        // ...
        // String qrCodeUrl = wxPayService.createOrder(request);

        return paymentId;
    }

    /**
     * 查询支付状态
     * 生产环境：调用微信支付查询订单接口
     */
    public String checkPaymentStatus(String paymentId) {
        // 从缓存获取状态
        String status = paymentStatusCache.get(paymentId);

        if (status == null) {
            // 查询数据库
            Payment payment = paymentMapper.findById(paymentId);
            if (payment == null) {
                return "failed";
            }
            status = payment.getVerified() ? "paid" : "pending";
            paymentStatusCache.put(paymentId, status);
        }

        // 生产环境：调用微信支付查询订单API
        // WxPayOrderQueryV3Result result = wxPayService.queryOrder(paymentId);
        // return result.getTradeState();

        return status;
    }

    /**
     * 标记支付成功（模拟）
     * 生产环境：由微信支付回调接口调用
     */
    public void markPaymentSuccess(String paymentId) {
        paymentStatusCache.put(paymentId, "paid");

        // 标记数据库为已验证
        Payment payment = paymentMapper.findById(paymentId);
        if (payment != null && !payment.getVerified()) {
            paymentMapper.verify(paymentId);

            // 通过UserService解锁内容，确保解锁状态正确
            userService.unlockContent(payment.getUserId(), payment.getContent());

            // 发送WebSocket实时通知
            webSocketHandler.notifyUnlock(payment.getUserId(), payment.getContent());
        }
    }

    public Payment submitPayment(String userId, String transactionId, Integer amount, String content) {
        Payment payment = new Payment()
                .setId(UUID.randomUUID().toString())
                .setUserId(userId)
                .setTransactionId(transactionId)
                .setAmount(amount)
                .setContent(content)
                .setCreateTime(LocalDateTime.now());

        paymentMapper.insert(payment);
        return payment;
    }

    public boolean verifyPayment(String paymentId, String userId) {
        System.out.println("🔍 开始验证支付 - paymentId: " + paymentId + ", userId: " + userId);

        Payment payment = paymentMapper.findPending().stream()
                .filter(p -> p.getId().equals(paymentId))
                .findFirst().orElse(null);

        if (payment != null && payment.getUserId().equals(userId)) {
            System.out.println("✅ 找到支付记录: " + payment);
            System.out.println("💰 支付内容: " + payment.getContent());

            paymentMapper.verify(paymentId);
            System.out.println("✅ 数据库支付状态已更新为已验证");

            // 通过UserService解锁
            userService.unlockContent(userId, payment.getContent());

            // 发送WebSocket实时通知
            webSocketHandler.notifyUnlock(userId, payment.getContent());
            System.out.println("📡 已发送WebSocket通知");

            return true;
        }

        System.err.println("❌ 支付验证失败 - 未找到支付记录或用户ID不匹配");
        return false;
    }

    public boolean hasAccess(String userId, String content) {
        // 委托给UserService检查权限
        return userService.hasAccess(userId, content);
    }

    public List<Payment> getPendingPayments() {
        return paymentMapper.findPending();
    }

    public List<Payment> getAllPayments() {
        return paymentMapper.findAll();
    }
}
