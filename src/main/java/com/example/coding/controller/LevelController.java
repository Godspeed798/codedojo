package com.example.coding.controller;

import com.example.coding.entity.Level;
import com.example.coding.entity.Result;
import com.example.coding.service.LevelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/level")
@CrossOrigin
public class LevelController {

    private final LevelService levelService;

    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @GetMapping
    public Result<List<Level>> getAllLevels() {
        return Result.ok(levelService.getAllLevels());
    }

    @GetMapping("/{id}")
    public Result<Level> getLevel(@PathVariable Integer id) {
        return Result.ok(levelService.getLevel(id));
    }

    @GetMapping("/type/{type}")
    public Result<List<Level>> getLevelsByType(@PathVariable String type) {
        return Result.ok(levelService.getLevelsByType(type));
    }
}
