package com.example.coding.mapper;

import com.example.coding.entity.Payment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaymentMapper {

    @Insert("INSERT INTO payment (id, user_id, transaction_id, amount, content, create_time, verified) " +
            "VALUES (#{id}, #{userId}, #{transactionId}, #{amount}, #{content}, #{createTime}, false)")
    void insert(Payment payment);

    @Select("SELECT p.*, u.username as user_name FROM payment p " +
            "LEFT JOIN app_user u ON p.user_id = u.id WHERE p.id = #{id}")
    @Result(property = "userName", column = "user_name")
    Payment findById(String id);

    @Select("SELECT p.*, u.username as user_name FROM payment p " +
            "LEFT JOIN app_user u ON p.user_id = u.id WHERE p.verified = false ORDER BY p.create_time")
    @Result(property = "userName", column = "user_name")
    List<Payment> findPending();

    @Update("UPDATE payment SET verified = true WHERE id = #{id}")
    void verify(String id);

    @Select("SELECT COUNT(*) FROM payment WHERE user_id = #{userId} AND verified = true")
    int countVerifiedByUser(String userId);

    @Select("SELECT p.*, u.username as user_name FROM payment p " +
            "LEFT JOIN app_user u ON p.user_id = u.id ORDER BY p.create_time DESC")
    @Result(property = "userName", column = "user_name")
    List<Payment> findAll();
}
