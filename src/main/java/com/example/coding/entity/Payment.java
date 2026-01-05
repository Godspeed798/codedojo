package com.example.coding.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Payment {
    private String id;
    private String userId;
    private String transactionId;
    private Integer amount;
    private String content;
    private LocalDateTime createTime;
    private Boolean verified;
}
