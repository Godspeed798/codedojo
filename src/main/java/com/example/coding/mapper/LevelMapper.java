package com.example.coding.mapper;

import com.example.coding.entity.Level;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LevelMapper {

    @Select("SELECT * FROM level ORDER BY id")
    List<Level> findAll();

    @Select("SELECT * FROM level WHERE id = #{id}")
    Level findById(Integer id);

    @Select("SELECT * FROM level WHERE type = #{type} ORDER BY id")
    List<Level> findByType(String type);

    @Insert("INSERT INTO level (id, name, type, difficulty, is_free, description, starter_code, solution, time_limit, exp_reward, coin_reward) " +
            "VALUES (#{id}, #{name}, #{type}, #{difficulty}, #{isFree}, #{description}, #{starterCode}, #{solution}, #{timeLimit}, #{expReward}, #{coinReward})")
    void insert(Level level);
}
