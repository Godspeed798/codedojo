package com.example.coding.service;

import com.example.coding.entity.User;
import com.example.coding.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final Map<String, Set<Integer>> unlockedLevels = new HashMap<>();

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getOrCreate(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            user = new User()
                    .setId(UUID.randomUUID().toString())
                    .setUsername(username)
                    .setLevel(1)
                    .setExp(0)
                    .setCoins(100)
                    .setEquipment("[]")
                    .setHintsToday(3)
                    .setAchievements("[]");
            userMapper.insert(user);
            unlockedLevels.put(user.getId(), new HashSet<>());
            for (int i = 1; i <= 5; i++) unlockedLevels.get(user.getId()).add(i);
        } else {
            unlockedLevels.putIfAbsent(user.getId(), new HashSet<>());
            for (int i = 1; i <= 5; i++) {
                if (!unlockedLevels.containsKey(user.getId())) {
                    unlockedLevels.put(user.getId(), new HashSet<>());
                }
                unlockedLevels.get(user.getId()).add(i);
            }
        }
        return user;
    }

    public User addExp(String userId, int exp) {
        User user = userMapper.findById(userId);
        user.setExp(user.getExp() + exp);
        int required = 100 * user.getLevel();
        if (user.getExp() >= required) {
            user.setLevel(user.getLevel() + 1);
            user.setExp(user.getExp() - required);
        }
        userMapper.update(user);
        return user;
    }

    public User addCoins(String userId, int coins) {
        User user = userMapper.findById(userId);
        user.setCoins(user.getCoins() + coins);
        userMapper.update(user);
        return user;
    }

    public boolean useHint(String userId) {
        User user = userMapper.findById(userId);
        if (user.getHintsToday() > 0) {
            user.setHintsToday(user.getHintsToday() - 1);
            userMapper.update(user);
            return true;
        }
        return false;
    }

    public boolean hasAccess(String userId, int levelId) {
        return unlockedLevels.getOrDefault(userId, Set.of()).contains(levelId);
    }

    public void unlockLevel(String userId, int levelId) {
        unlockedLevels.computeIfAbsent(userId, k -> new HashSet<>()).add(levelId);
    }
}
