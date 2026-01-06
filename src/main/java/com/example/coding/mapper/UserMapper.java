package com.example.coding.mapper;

import com.example.coding.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM app_user WHERE id = #{id}")
    User findById(String id);

    @Select("SELECT * FROM app_user WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO app_user (id, username, level, exp, coins, equipment, hints_today, achievements, unlocked_content) " +
            "VALUES (#{id}, #{username}, #{level}, #{exp}, #{coins}, #{equipment}, #{hintsToday}, #{achievements}, #{unlockedContent})")
    void insert(User user);

    @Update("UPDATE app_user SET level = #{level}, exp = #{exp}, coins = #{coins}, " +
            "equipment = #{equipment}, hints_today = #{hintsToday}, achievements = #{achievements}, " +
            "unlocked_content = #{unlockedContent} " +
            "WHERE id = #{id}")
    void update(User user);
}
