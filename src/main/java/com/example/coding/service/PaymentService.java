package com.example.coding.service;

import com.example.coding.entity.Payment;
import com.example.coding.mapper.PaymentMapper;
import com.example.coding.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final UserMapper userMapper;
    private final Map<String, Set<String>> userUnlocks = new HashMap<>();

    public PaymentService(PaymentMapper paymentMapper, UserMapper userMapper) {
        this.paymentMapper = paymentMapper;
        this.userMapper = userMapper;
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
        Payment payment = paymentMapper.findPending().stream()
                .filter(p -> p.getId().equals(paymentId))
                .findFirst().orElse(null);

        if (payment != null && payment.getUserId().equals(userId)) {
            paymentMapper.verify(paymentId);
            userUnlocks.computeIfAbsent(userId, k -> new HashSet<>()).add(payment.getContent());
            return true;
        }
        return false;
    }

    public boolean hasAccess(String userId, String content) {
        Set<String> unlocks = userUnlocks.get(userId);
        if (unlocks != null && unlocks.contains(content)) {
            return true;
        }
        return "basic".equals(content);
    }

    public List<Payment> getPendingPayments() {
        return paymentMapper.findPending();
    }
}
