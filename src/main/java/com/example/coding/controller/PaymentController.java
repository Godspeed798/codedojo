package com.example.coding.controller;

import com.example.coding.entity.Payment;
import com.example.coding.entity.Result;
import com.example.coding.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * 创建支付订单
     * 返回订单ID，前端用于查询支付状态
     */
    @PostMapping("/create")
    public Result<String> createPayment(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        // 修复类型转换：JSON数字可能被解析为Double
        Number amountNum = (Number) request.get("amount");
        Integer amount = amountNum != null ? amountNum.intValue() : 0;
        String content = (String) request.get("content");

        String paymentId = paymentService.createPayment(userId, amount, content);
        return Result.ok(paymentId);
    }

    /**
     * 查询支付状态
     * 返回: pending(等待支付), paid(已支付), failed(支付失败)
     */
    @GetMapping("/status/{paymentId}")
    public Result<String> getPaymentStatus(@PathVariable String paymentId) {
        String status = paymentService.checkPaymentStatus(paymentId);
        return Result.ok(status);
    }

    /**
     * 模拟支付成功（仅用于测试）
     * 生产环境应该由微信支付回调通知
     */
    @PostMapping("/{paymentId}/simulate-success")
    public Result<Boolean> simulatePaymentSuccess(@PathVariable String paymentId) {
        paymentService.markPaymentSuccess(paymentId);
        return Result.ok(true);
    }

    @PostMapping
    public Result<Payment> submitPayment(@RequestBody Map<String, String> request) {
        Payment payment = paymentService.submitPayment(
                request.get("userId"),
                request.get("transactionId"),
                Integer.parseInt(request.getOrDefault("amount", "0")),
                request.get("content")
        );
        return Result.ok(payment);
    }

    @GetMapping("/pending")
    public Result<List<Payment>> getPending() {
        return Result.ok(paymentService.getPendingPayments());
    }

    @PostMapping("/{paymentId}/verify")
    public Result<Boolean> verifyPayment(@PathVariable String paymentId, @RequestBody Map<String, String> request) {
        return Result.ok(paymentService.verifyPayment(paymentId, request.get("userId")));
    }

    @GetMapping("/{userId}/check/{content}")
    public Result<Boolean> checkAccess(@PathVariable String userId, @PathVariable String content) {
        return Result.ok(paymentService.hasAccess(userId, content));
    }

    @GetMapping("/items")
    public Result<List<Map<String, Object>>> getItems() {
        return Result.ok(List.of(
                Map.of("id", "sorting", "name", "排序火山", "price", 666, "content", "sorting"),
                Map.of("id", "search", "name", "查找地牢", "price", 888, "content", "search"),
                Map.of("id", "bundle", "name", "副本大礼包", "price", 1500, "content", "all")
        ));
    }

    /**
     * 获取所有支付订单（用于管理后台）
     */
    @GetMapping("/all")
    public Result<List<Payment>> getAllPayments() {
        return Result.ok(paymentService.getAllPayments());
    }
}
