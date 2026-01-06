package com.example.coding.service;

import com.example.coding.entity.User;
import com.example.coding.mapper.UserMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    // 保留用于兼容旧代码，但不再作为主要数据源
    private final Map<String, Set<Integer>> unlockedLevels = new HashMap<>();

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 获取用户已解锁的内容列表
     */
    private Set<String> getUnlockedContents(User user) {
        if (user.getUnlockedContent() == null || user.getUnlockedContent().isEmpty()) {
            return new HashSet<>();
        }
        try {
            List<String> contents = objectMapper.readValue(user.getUnlockedContent(), new TypeReference<List<String>>() {});
            return new HashSet<>(contents);
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    /**
     * 保存用户已解锁的内容列表
     */
    private void saveUnlockedContents(User user, Set<String> contents) {
        try {
            String json = objectMapper.writeValueAsString(new ArrayList<>(contents));
            user.setUnlockedContent(json);
            userMapper.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    .setAchievements("[]")
                    .setUnlockedContent("[]");  // 初始化为空数组
            userMapper.insert(user);
            unlockedLevels.put(user.getId(), new HashSet<>());
            for (int i = 1; i <= 5; i++) unlockedLevels.get(user.getId()).add(i);
        } else {
            // 确保已有用户的 hintsToday 字段有默认值
            if (user.getHintsToday() == null) {
                user.setHintsToday(3);
                userMapper.update(user);
            }
            // 确保unlockedContent字段存在
            if (user.getUnlockedContent() == null) {
                user.setUnlockedContent("[]");
                userMapper.update(user);
            }
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
        // 关卡1-5默认开放
        if (levelId >= 1 && levelId <= 5) {
            return true;
        }
        // 检查数据库中的解锁状态
        User user = userMapper.findById(userId);
        if (user == null) {
            return false;
        }
        Set<String> unlockedContents = getUnlockedContents(user);

        // 根据关卡ID范围检查权限
        if (levelId >= 6 && levelId <= 13) {
            // 排序火山
            return unlockedContents.contains("sorting") || unlockedContents.contains("all");
        } else if (levelId >= 14 && levelId <= 22) {
            // 查找地牢
            return unlockedContents.contains("search") || unlockedContents.contains("all");
        }
        return false;
    }

    public boolean hasAccess(String userId, String content) {
        // 解析内容类型：sorting, search, all, 或 basic
        if ("basic".equals(content)) {
            return true; // 新手村默认开放
        }

        User user = userMapper.findById(userId);
        if (user == null) {
            return false;
        }

        Set<String> unlockedContents = getUnlockedContents(user);

        switch (content) {
            case "sorting":
                return unlockedContents.contains("sorting") || unlockedContents.contains("all");
            case "search":
                return unlockedContents.contains("search") || unlockedContents.contains("all");
            case "all":
                return unlockedContents.contains("all");
            default:
                return false;
        }
    }

    /**
     * 获取用户的所有已解锁内容（用于前端显示）
     * 返回List而不是Set，确保JSON序列化后前端可以正确处理
     */
    public List<String> getUnlockedContents(String userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            return new ArrayList<>();
        }
        Set<String> contents = getUnlockedContents(user);
        List<String> result = new ArrayList<>(contents);
        System.out.println("📦 用户 " + userId + " 的解锁状态: " + result);
        return result;
    }

    public void unlockLevel(String userId, int levelId) {
        unlockedLevels.computeIfAbsent(userId, k -> new HashSet<>()).add(levelId);
    }

    public void unlockContent(String userId, String content) {
        User user = userMapper.findById(userId);
        if (user == null) {
            System.err.println("❌ 用户不存在: " + userId);
            return;
        }

        System.out.println("🔓 开始解锁内容 - 用户ID: " + userId + ", 内容: " + content);

        // 获取当前已解锁的内容
        Set<String> unlockedContents = getUnlockedContents(user);
        System.out.println("📋 当前解锁状态: " + unlockedContents);

        // 解锁指定类型的所有关卡
        switch (content) {
            case "sorting":
                // 解锁排序火山
                unlockedContents.add("sorting");
                for (int i = 6; i <= 13; i++) {
                    unlockLevel(userId, i);
                }
                System.out.println("✅ 已解锁排序火山（关卡6-13）");
                break;
            case "search":
                // 解锁查找地牢
                unlockedContents.add("search");
                for (int i = 14; i <= 22; i++) {
                    unlockLevel(userId, i);
                }
                System.out.println("✅ 已解锁查找地牢（关卡14-22）");
                break;
            case "all":
                // 解锁所有付费关卡
                unlockedContents.add("all");
                for (int i = 6; i <= 22; i++) {
                    unlockLevel(userId, i);
                }
                System.out.println("✅ 已解锁全部付费内容（关卡6-22）");
                break;
            default:
                // 如果是关卡ID（支持 "level_2" 格式或纯数字 "2"）
                try {
                    int levelId;
                    if (content.startsWith("level_")) {
                        levelId = Integer.parseInt(content.substring(6)); // 去掉 "level_" 前缀
                    } else {
                        levelId = Integer.parseInt(content);
                    }
                    unlockLevel(userId, levelId);
                    System.out.println("✅ 已解锁单个关卡: " + levelId);
                    // 单个关卡解锁不添加到unlockedContents
                    return;
                } catch (NumberFormatException e) {
                    System.err.println("❌ 无效的内容类型: " + content);
                    return;
                }
        }

        // 保存到数据库
        System.out.println("💾 保存解锁状态到数据库: " + unlockedContents);
        saveUnlockedContents(user, unlockedContents);
        System.out.println("✅ 解锁内容保存完成");
    }
}
