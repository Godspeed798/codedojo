package com.example.coding.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
    private String id;
    private String username;
    private Integer level;
    private Integer exp;
    private Integer coins;
    private String equipment;
    private Integer hintsToday;
    private String achievements;
}
