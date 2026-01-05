package com.example.coding.service;

import com.example.coding.entity.Level;
import com.example.coding.mapper.LevelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LevelService {

    private final LevelMapper levelMapper;

    public LevelService(LevelMapper levelMapper) {
        this.levelMapper = levelMapper;
        initLevels();
    }

    private void initLevels() {
        List<Level> levels = new ArrayList<>();

        levels.add(new Level().setId(1).setName("变量峡谷 - 第1关").setType("basic").setDifficulty(1)
                .setIsFree(true).setDescription("声明一个整数变量 x = 10")
                .setStarterCode("int x = ___;\nSystem.out.println(x);")
                .setSolution("int x = 10;").setTimeLimit(60).setExpReward(10).setCoinReward(5));

        levels.add(new Level().setId(2).setName("变量峡谷 - 第2关").setType("basic").setDifficulty(1)
                .setIsFree(true).setDescription("声明两个变量并求和")
                .setStarterCode("int a = 5, b = 3;\nint sum = ___;\nSystem.out.println(sum);")
                .setSolution("int sum = a + b;").setTimeLimit(60).setExpReward(15).setCoinReward(8));

        levels.add(new Level().setId(3).setName("循环河流 - 第1关").setType("basic").setDifficulty(2)
                .setIsFree(true).setDescription("使用for循环打印1到5")
                .setStarterCode("for (int i = 1; i <= ___; i++) {\n    System.out.println(i);\n}")
                .setSolution("for (int i = 1; i <= 5; i++) {").setTimeLimit(90).setExpReward(20).setCoinReward(10));

        levels.add(new Level().setId(4).setName("循环河流 - 第2关").setType("basic").setDifficulty(2)
                .setIsFree(true).setDescription("使用while循环求1到10的和")
                .setStarterCode("int i = 1, sum = 0;\nwhile (i <= ___) {\n    sum += i;\n    i++;\n}")
                .setSolution("while (i <= 10) {").setTimeLimit(90).setExpReward(25).setCoinReward(12));

        levels.add(new Level().setId(5).setName("函数山峰 - 第1关").setType("basic").setDifficulty(2)
                .setIsFree(true).setDescription("实现一个返回两数最大值的函数")
                .setStarterCode("int max(int a, int b) {\n    if (___ > b) return a;\n    return b;\n}")
                .setSolution("if (a > b) return a;").setTimeLimit(120).setExpReward(30).setCoinReward(15));

        levels.add(new Level().setId(6).setName("排序火山 - 冒泡排序").setType("sorting").setDifficulty(4)
                .setIsFree(false).setDescription("实现冒泡排序算法")
                .setStarterCode("void bubbleSort(int[] arr) {\n    for (int i = 0; i < arr.length - 1; i++) {\n        for (int j = 0; j < arr.length - 1 - i; j++) {\n            if (arr[j] > arr[j + 1]) {\n                int temp = arr[j];\n                arr[j] = ___;\n                arr[j + 1] = temp;\n            }\n        }\n    }\n}")
                .setSolution("arr[j + 1] = temp;").setTimeLimit(180).setExpReward(50).setCoinReward(25));

        levels.add(new Level().setId(7).setName("排序火山 - 选择排序").setType("sorting").setDifficulty(5)
                .setIsFree(false).setDescription("实现选择排序算法")
                .setStarterCode("void selectionSort(int[] arr) {\n    for (int i = 0; i < arr.length - 1; i++) {\n        int minIdx = i;\n        for (int j = i + 1; j < arr.length; j++) {\n            if (arr[j] < arr[minIdx]) {\n                ___ = j;\n            }\n        }\n        int temp = arr[i]; arr[i] = arr[minIdx]; arr[minIdx] = temp;\n    }\n}")
                .setSolution("minIdx = j;").setTimeLimit(180).setExpReward(60).setCoinReward(30));

        levels.add(new Level().setId(8).setName("查找地牢 - 二分查找").setType("search").setDifficulty(5)
                .setIsFree(false).setDescription("实现二分查找算法")
                .setStarterCode("int binarySearch(int[] arr, int target) {\n    int left = 0, right = arr.length - 1;\n    while (left <= right) {\n        int mid = left + (right - left) / 2;\n        if (arr[mid] == target) return mid;\n        if (arr[mid] < target) left = ___ + 1;\n        else right = mid - 1;\n    }\n    return -1;\n}")
                .setSolution("left = mid + 1;").setTimeLimit(180).setExpReward(70).setCoinReward(35));

        for (Level level : levels) {
            try {
                levelMapper.insert(level);
            } catch (Exception e) {}
        }
    }

    public List<Level> getAllLevels() {
        return levelMapper.findAll();
    }

    public Level getLevel(Integer id) {
        return levelMapper.findById(id);
    }

    public List<Level> getLevelsByType(String type) {
        return levelMapper.findByType(type);
    }
}
