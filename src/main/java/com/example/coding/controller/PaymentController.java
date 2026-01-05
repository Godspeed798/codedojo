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
}
