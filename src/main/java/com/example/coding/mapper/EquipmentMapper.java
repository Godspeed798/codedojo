package com.example.coding.mapper;

import com.example.coding.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EquipmentMapper {

    @Select("SELECT * FROM equipment ORDER BY price")
    List<Equipment> findAll();

    @Select("SELECT * FROM equipment WHERE id = #{id}")
    Equipment findById(Integer id);

    @Select("SELECT * FROM equipment WHERE type = #{type} ORDER BY price")
    List<Equipment> findByType(String type);
}
