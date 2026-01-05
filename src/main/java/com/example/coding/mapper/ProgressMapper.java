package com.example.coding.mapper;

import com.example.coding.entity.Progress;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProgressMapper {

    @Select("SELECT * FROM progress WHERE user_id = #{userId}")
    List<Progress> findByUserId(String userId);

    @Select("SELECT * FROM progress WHERE user_id = #{userId} AND level_id = #{levelId}")
    Progress findByUserAndLevel(@Param("userId") String userId, @Param("levelId") Integer levelId);

    @Insert("INSERT INTO progress (user_id, level_id, completed, stars, best_time) " +
            "VALUES (#{userId}, #{levelId}, #{completed}, #{stars}, #{bestTime})")
    void insert(Progress progress);

    @Update("UPDATE progress SET completed = #{completed}, stars = #{stars}, best_time = #{bestTime} " +
            "WHERE user_id = #{userId} AND level_id = #{levelId}")
    void update(Progress progress);
}
