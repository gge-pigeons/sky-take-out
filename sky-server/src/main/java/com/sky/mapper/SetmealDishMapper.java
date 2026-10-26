package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    //根据菜品id查询套餐id
    //select setmeal_id from setmeal_dish where dish_id in(1,2,3,4)
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 插入套餐与菜品关系表
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
