package com.example.coding.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Level {
    private Integer id;
    private String name;
    private String type;
    private Integer difficulty;
    private Boolean isFree;
    private String description;
    private String starterCode;
    private String solution;
    private Integer timeLimit;
    private Integer expReward;
    private Integer coinReward;
}
