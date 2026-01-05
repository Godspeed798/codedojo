package com.example.coding.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlgorithmService {

    public List<Map<String, Object>> bubbleSort(int[] arr) {
        List<Map<String, Object>> steps = new ArrayList<>();
        int[] data = arr.clone();
        int n = data.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                Map<String, Object> step = new HashMap<>();
                step.put("step", steps.size() + 1);
                step.put("data", data.clone());
                step.put("compare", Arrays.asList(j, j + 1));
                step.put("swapping", data[j] > data[j + 1]);
                step.put("description", "比较 " + data[j] + " 和 " + data[j + 1]);

                if (data[j] > data[j + 1]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
                steps.add(step);
            }
            Map<String, Object> step = new HashMap<>();
            step.put("step", steps.size() + 1);
            step.put("data", data.clone());
            step.put("sorted", n - 1 - i);
            step.put("description", "位置 " + (n - 1 - i) + " 已确定");
            steps.add(step);
        }

        return steps;
    }

    public List<Map<String, Object>> selectionSort(int[] arr) {
        List<Map<String, Object>> steps = new ArrayList<>();
        int[] data = arr.clone();
        int n = data.length;

        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                Map<String, Object> step = new HashMap<>();
                step.put("step", steps.size() + 1);
                step.put("data", data.clone());
                step.put("compare", Arrays.asList(minIdx, j));
                step.put("currentMin", minIdx);
                step.put("description", "寻找最小值，当前最小: " + data[minIdx]);
                steps.add(step);

                if (data[j] < data[minIdx]) {
                    minIdx = j;
                }
            }

            Map<String, Object> step = new HashMap<>();
            step.put("step", steps.size() + 1);
            step.put("data", data.clone());
            step.put("swap", Arrays.asList(i, minIdx));
            step.put("description", "交换 " + i + " 和最小值位置 " + minIdx);
            steps.add(step);

            int temp = data[i];
            data[i] = data[minIdx];
            data[minIdx] = temp;
        }

        return steps;
    }

    public List<Map<String, Object>> quickSort(int[] arr) {
        List<Map<String, Object>> steps = new ArrayList<>();
        int[] data = arr.clone();
        quickSortHelper(data, 0, data.length - 1, steps);
        return steps;
    }

    private void quickSortHelper(int[] arr, int low, int high, List<Map<String, Object>> steps) {
        if (low < high) {
            int pi = partition(arr, low, high, steps);
            quickSortHelper(arr, low, pi - 1, steps);
            quickSortHelper(arr, pi + 1, high, steps);
        }
    }

    private int partition(int[] arr, int low, int high, List<Map<String, Object>> steps) {
        int pivot = arr[high];
        int i = low - 1;

        Map<String, Object> pivotStep = new HashMap<>();
        pivotStep.put("step", steps.size() + 1);
        pivotStep.put("data", arr.clone());
        pivotStep.put("pivot", high);
        pivotStep.put("description", "选择基准值: " + pivot);
        steps.add(pivotStep);

        for (int j = low; j < high; j++) {
            Map<String, Object> step = new HashMap<>();
            step.put("step", steps.size() + 1);
            step.put("data", arr.clone());
            step.put("compare", Arrays.asList(j, high));
            step.put("pivot", high);
            step.put("description", "比较 " + arr[j] + " 与基准值 " + pivot);
            steps.add(step);

            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;

                if (i != j) {
                    Map<String, Object> swapStep = new HashMap<>();
                    swapStep.put("step", steps.size() + 1);
                    swapStep.put("data", arr.clone());
                    swapStep.put("swap", Arrays.asList(i, j));
                    swapStep.put("pivot", high);
                    swapStep.put("description", "交换较小值到左边");
                    steps.add(swapStep);
                }
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        Map<String, Object> finalStep = new HashMap<>();
        finalStep.put("step", steps.size() + 1);
        finalStep.put("data", arr.clone());
        finalStep.put("pivotFinal", i + 1);
        finalStep.put("description", "基准值归位到位置 " + (i + 1));
        steps.add(finalStep);

        return i + 1;
    }

    public List<Map<String, Object>> race(int[] arr, String algo1, String algo2) {
        List<Map<String, Object>> result = new ArrayList<>();
        long start1 = System.nanoTime();
        List<Map<String, Object>> steps1 = getSteps(algo1, arr.clone());
        long end1 = System.nanoTime();

        long start2 = System.nanoTime();
        List<Map<String, Object>> steps2 = getSteps(algo2, arr.clone());
        long end2 = System.nanoTime();

        result.add(Map.of(
                "algo1", algo1,
                "time1", (double)(end1 - start1) / 1_000_000.0,
                "steps1", steps1.size(),
                "algo2", algo2,
                "time2", (double)(end2 - start2) / 1_000_000.0,
                "steps2", steps2.size(),
                "winner", (end1 - start1) < (end2 - start2) ? algo1 : algo2
        ));

        return result;
    }

    private List<Map<String, Object>> getSteps(String algo, int[] arr) {
        return switch (algo) {
            case "bubble" -> bubbleSort(arr);
            case "selection" -> selectionSort(arr);
            case "quick" -> quickSort(arr);
            default -> new ArrayList<>();
        };
    }
}
