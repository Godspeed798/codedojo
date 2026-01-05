package com.example.coding.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Equipment {
    private Integer id;
    private String name;
    private String type;
    private Integer price;
    private String effect;
    private Integer effectValue;
    private String rarity;
    private String icon;
    private Boolean isFree;
}
