package com.example.coding.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Progress {
    private String userId;
    private Integer levelId;
    private Boolean completed;
    private Integer stars;
    private Integer bestTime;
}
