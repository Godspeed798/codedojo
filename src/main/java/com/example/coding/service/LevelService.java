package com.example.coding.service;

import com.example.coding.entity.Level;
import com.example.coding.mapper.LevelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LevelService {

    private final LevelMapper levelMapper;

    public LevelService(LevelMapper levelMapper) {
        this.levelMapper = levelMapper;
        initLevels();
    }

    @Transactional
    private void initLevels() {
        List<Level> levels = new ArrayList<>();

        // 新手村 - 基础语法森林 (免费)
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

        // 排序火山 - 付费
        levels.add(new Level().setId(6).setName("排序火山 - 冒泡排序").setType("sorting").setDifficulty(4)
                .setIsFree(false).setDescription("实现冒泡排序算法")
                .setStarterCode("void bubbleSort(int[] arr) {\n    for (int i = 0; i < arr.length - 1; i++) {\n        for (int j = 0; j < arr.length - 1 - i; j++) {\n            if (arr[j] > arr[j + 1]) {\n                int temp = arr[j];\n                arr[j] = ___;\n                arr[j + 1] = temp;\n            }\n        }\n    }\n}")
                .setSolution("arr[j + 1] = temp;").setTimeLimit(180).setExpReward(50).setCoinReward(25));

        levels.add(new Level().setId(7).setName("排序火山 - 选择排序").setType("sorting").setDifficulty(5)
                .setIsFree(false).setDescription("实现选择排序算法")
                .setStarterCode("void selectionSort(int[] arr) {\n    for (int i = 0; i < arr.length - 1; i++) {\n        int minIdx = i;\n        for (int j = i + 1; j < arr.length; j++) {\n            if (arr[j] < arr[minIdx]) {\n                ___ = j;\n            }\n        }\n        int temp = arr[i]; arr[i] = arr[minIdx]; arr[minIdx] = temp;\n    }\n}")
                .setSolution("minIdx = j;").setTimeLimit(180).setExpReward(60).setCoinReward(30));

        levels.add(new Level().setId(8).setName("排序火山 - 插入排序").setType("sorting").setDifficulty(4)
                .setIsFree(false).setDescription("实现插入排序算法")
                .setStarterCode("void insertionSort(int[] arr) {\n    for (int i = 1; i < arr.length; i++) {\n        int key = arr[i];\n        int j = i - 1;\n        while (j >= 0 && arr[j] > ___) {\n            arr[j + 1] = arr[j];\n            j--;\n        }\n        arr[j + 1] = key;\n    }\n}")
                .setSolution("key").setTimeLimit(180).setExpReward(55).setCoinReward(28));

        levels.add(new Level().setId(9).setName("排序火山 - 希尔排序").setType("sorting").setDifficulty(6)
                .setIsFree(false).setDescription("实现希尔排序算法")
                .setStarterCode("void shellSort(int[] arr) {\n    for (int gap = arr.length / 2; gap > 0; gap /= 2) {\n        for (int i = gap; i < arr.length; i++) {\n            int temp = arr[i];\n            int j;\n            for (j = i; j >= gap && arr[j - gap] > ___; j -= gap) {\n                arr[j] = arr[j - gap];\n            }\n            arr[j] = temp;\n        }\n    }\n}")
                .setSolution("temp").setTimeLimit(200).setExpReward(70).setCoinReward(35));

        levels.add(new Level().setId(10).setName("排序火山 - 归并排序").setType("sorting").setDifficulty(7)
                .setIsFree(false).setDescription("实现归并排序算法")
                .setStarterCode("void merge(int[] arr, int l, int m, int r) {\n    int[] temp = new int[r - l + 1];\n    int i = l, j = m + 1, k = 0;\n    while (i <= m && j <= r) {\n        if (arr[i] <= arr[j]) temp[k++] = arr[i++];\n        else temp[k++] = ___;\n    }\n    while (i <= m) temp[k++] = arr[i++];\n    while (j <= r) temp[k++] = arr[j++];\n}")
                .setSolution("arr[j++]").setTimeLimit(240).setExpReward(80).setCoinReward(40));

        levels.add(new Level().setId(11).setName("排序火山 - 快速排序").setType("sorting").setDifficulty(7)
                .setIsFree(false).setDescription("实现快速排序算法")
                .setStarterCode("void quickSort(int[] arr, int low, int high) {\n    if (low < high) {\n        int pi = partition(arr, low, high);\n        quickSort(arr, low, ___ - 1);\n        quickSort(arr, pi + 1, high);\n    }\n}")
                .setSolution("pi").setTimeLimit(240).setExpReward(90).setCoinReward(45));

        levels.add(new Level().setId(12).setName("排序火山 - 堆排序").setType("sorting").setDifficulty(8)
                .setIsFree(false).setDescription("实现堆排序算法")
                .setStarterCode("void heapify(int[] arr, int n, int i) {\n    int largest = i;\n    int left = 2 * i + 1;\n    int right = 2 * i + 2;\n    if (left < n && arr[left] > arr[largest]) largest = ___;\n    if (right < n && arr[right] > arr[largest]) largest = right;\n}")
                .setSolution("left").setTimeLimit(240).setExpReward(100).setCoinReward(50));

        levels.add(new Level().setId(13).setName("排序火山 - 计数排序").setType("sorting").setDifficulty(5)
                .setIsFree(false).setDescription("实现计数排序算法")
                .setStarterCode("void countingSort(int[] arr) {\n    int max = Arrays.stream(arr).max().getAsInt();\n    int[] count = new int[max + 1];\n    for (int num : arr) count[num]++;\n    int idx = 0;\n    for (int i = 0; i <= max; i++) {\n        while (___ > 0) {\n            arr[idx++] = i;\n            count[i]--;\n        }\n    }\n}")
                .setSolution("count[i]").setTimeLimit(180).setExpReward(65).setCoinReward(33));

        // 查找地牢 - 付费
        levels.add(new Level().setId(14).setName("查找地牢 - 顺序查找").setType("search").setDifficulty(3)
                .setIsFree(false).setDescription("实现顺序查找算法")
                .setStarterCode("int linearSearch(int[] arr, int target) {\n    for (int i = 0; i < arr.___; i++) {\n        if (arr[i] == target) return i;\n    }\n    return -1;\n}")
                .setSolution("length").setTimeLimit(120).setExpReward(40).setCoinReward(20));

        levels.add(new Level().setId(15).setName("查找地牢 - 二分查找").setType("search").setDifficulty(5)
                .setIsFree(false).setDescription("实现二分查找算法")
                .setStarterCode("int binarySearch(int[] arr, int target) {\n    int left = 0, right = arr.length - 1;\n    while (left <= right) {\n        int mid = left + (right - left) / 2;\n        if (arr[mid] == target) return mid;\n        if (arr[mid] < target) left = ___ + 1;\n        else right = mid - 1;\n    }\n    return -1;\n}")
                .setSolution("left = mid + 1;").setTimeLimit(180).setExpReward(70).setCoinReward(35));

        levels.add(new Level().setId(16).setName("查找地牢 - 插值查找").setType("search").setDifficulty(6)
                .setIsFree(false).setDescription("实现插值查找算法")
                .setStarterCode("int interpolationSearch(int[] arr, int target) {\n    int left = 0, right = arr.length - 1;\n    while (left <= right && target >= arr[left] && target <= arr[right]) {\n        int pos = left + ((target - arr[left]) * (right - left)) / (arr[right] - ___);\n        if (arr[pos] == target) return pos;\n        if (arr[pos] < target) left = pos + 1;\n        else right = pos - 1;\n    }\n    return -1;\n}")
                .setSolution("arr[left]").setTimeLimit(200).setExpReward(75).setCoinReward(38));

        levels.add(new Level().setId(17).setName("查找地牢 - 斐波那契查找").setType("search").setDifficulty(7)
                .setIsFree(false).setDescription("实现斐波那契查找算法")
                .setStarterCode("int fibonacciSearch(int[] arr, int target) {\n    int fibM2 = 0, fibM1 = 1, fibM = fibM2 + fibM1;\n    while (fibM < arr.length) {\n        fibM2 = ___;\n        fibM1 = fibM;\n        fibM = fibM2 + fibM1;\n    }\n}")
                .setSolution("fibM1").setTimeLimit(220).setExpReward(85).setCoinReward(43));

        levels.add(new Level().setId(18).setName("查找地牢 - 哈希查找").setType("search").setDifficulty(5)
                .setIsFree(false).setDescription("实现哈希表查找")
                .setStarterCode("int hashSearch(int[] arr, int target) {\n    int size = arr.length;\n    Map<Integer, Integer> map = new HashMap<>();\n    for (int i = 0; i < size; i++) {\n        map.put(___, i);\n    }\n    return map.getOrDefault(target, -1);\n}")
                .setSolution("arr[i]").setTimeLimit(180).setExpReward(60).setCoinReward(30));

        levels.add(new Level().setId(19).setName("查找地牢 - 二叉搜索树查找").setType("search").setDifficulty(7)
                .setIsFree(false).setDescription("实现BST查找")
                .setStarterCode("TreeNode search(TreeNode root, int target) {\n    if (root == null || root.val == target) return ___;\n    if (root.val < target) return search(root.right, target);\n    return search(root.left, target);\n}")
                .setSolution("root").setTimeLimit(200).setExpReward(80).setCoinReward(40));

        levels.add(new Level().setId(20).setName("查找地牢 - 跳表查找").setType("search").setDifficulty(8)
                .setIsFree(false).setDescription("实现跳表查找")
                .setStarterCode("Node skipListSearch(Node head, int target) {\n    Node curr = head;\n    while (curr != null) {\n        if (curr.val == target) return ___;\n        if (curr.next != null && curr.next.val <= target) curr = curr.next;\n        else curr = curr.down;\n    }\n    return null;\n}")
                .setSolution("curr").setTimeLimit(240).setExpReward(95).setCoinReward(48));

        levels.add(new Level().setId(21).setName("查找地牢 - BFS广度优先").setType("search").setDifficulty(6)
                .setIsFree(false).setDescription("实现BFS查找")
                .setStarterCode("int bfsSearch(Map<Integer, List<Integer>> graph, int start, int target) {\n    Queue<Integer> queue = new LinkedList<>();\n    Set<Integer> visited = new HashSet<>();\n    queue.offer(___);\n    visited.add(start);\n    int steps = 0;\n}")
                .setSolution("start").setTimeLimit(200).setExpReward(70).setCoinReward(35));

        levels.add(new Level().setId(22).setName("查找地牢 - DFS深度优先").setType("search").setDifficulty(6)
                .setIsFree(false).setDescription("实现DFS查找")
                .setStarterCode("boolean dfsSearch(Map<Integer, List<Integer>> graph, int node, int target, Set<Integer> visited) {\n    if (node == target) return ___;\n    visited.add(node);\n    for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {\n        if (!visited.contains(neighbor)) {\n            if (dfsSearch(graph, neighbor, target, visited)) return true;\n        }\n    }\n    return false;\n}")
                .setSolution("true").setTimeLimit(200).setExpReward(70).setCoinReward(35));

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
