package com.example.coding.controller;

import com.example.coding.entity.Result;
import com.example.coding.service.AlgorithmService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/algorithm")
@CrossOrigin
public class AlgorithmController {

    private final AlgorithmService algorithmService;

    public AlgorithmController(AlgorithmService algorithmService) {
        this.algorithmService = algorithmService;
    }

    @PostMapping("/bubble")
    public Result<List<Map<String, Object>>> bubbleSort(@RequestBody int[] arr) {
        return Result.ok(algorithmService.bubbleSort(arr));
    }

    @PostMapping("/selection")
    public Result<List<Map<String, Object>>> selectionSort(@RequestBody int[] arr) {
        return Result.ok(algorithmService.selectionSort(arr));
    }

    @PostMapping("/quick")
    public Result<List<Map<String, Object>>> quickSort(@RequestBody int[] arr) {
        return Result.ok(algorithmService.quickSort(arr));
    }

    @PostMapping("/race")
    public Result<List<Map<String, Object>>> race(@RequestBody Map<String, Object> request) {
        int[] arr = ((List<Integer>) request.get("arr")).stream().mapToInt(i -> i).toArray();
        String algo1 = (String) request.get("algo1");
        String algo2 = (String) request.get("algo2");
        return Result.ok(algorithmService.race(arr, algo1, algo2));
    }
}
