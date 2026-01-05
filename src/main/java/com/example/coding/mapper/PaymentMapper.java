package com.example.coding.mapper;

import com.example.coding.entity.Payment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaymentMapper {

    @Insert("INSERT INTO payment (id, user_id, transaction_id, amount, content, create_time, verified) " +
            "VALUES (#{id}, #{userId}, #{transactionId}, #{amount}, #{content}, #{createTime}, false)")
    void insert(Payment payment);

    @Select("SELECT * FROM payment WHERE verified = false ORDER BY create_time")
    List<Payment> findPending();

    @Update("UPDATE payment SET verified = true WHERE id = #{id}")
    void verify(String id);

    @Select("SELECT COUNT(*) FROM payment WHERE user_id = #{userId} AND verified = true")
    int countVerifiedByUser(String userId);
}
