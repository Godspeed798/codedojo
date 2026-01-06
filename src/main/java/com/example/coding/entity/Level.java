package com.example.coding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    public Boolean getFree() {
        return isFree;
    }

    public void setFree(Boolean free) {
        this.isFree = free;
    }

    @JsonProperty("free")
    public Boolean getJsonFree() {
        return isFree;
    }

    public void setJsonFree(Boolean free) {
        this.isFree = free;
    }
}
