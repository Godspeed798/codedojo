package com.example.coding.controller;

import com.example.coding.entity.Result;
import com.example.coding.entity.User;
import com.example.coding.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public Result<User> getUser(@PathVariable String username) {
        return Result.ok(userService.getOrCreate(username));
    }

    @PostMapping("/{userId}/exp")
    public Result<User> addExp(@PathVariable String userId, @RequestParam int exp) {
        return Result.ok(userService.addExp(userId, exp));
    }

    @PostMapping("/{userId}/coins")
    public Result<User> addCoins(@PathVariable String userId, @RequestParam int coins) {
        return Result.ok(userService.addCoins(userId, coins));
    }

    @PostMapping("/{userId}/hint")
    public Result<Boolean> useHint(@PathVariable String userId) {
        return Result.ok(userService.useHint(userId));
    }

    @GetMapping("/{userId}/access/{levelId}")
    public Result<Boolean> checkAccess(@PathVariable String userId, @PathVariable Integer levelId) {
        return Result.ok(userService.hasAccess(userId, levelId));
    }
}
