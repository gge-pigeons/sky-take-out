package com.sky.mapper;

import com.sky.entity.SetmealDish;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    //根据菜品id查询套餐id
    //select setmeal_id from setmeal_dish where dish_id in(1,2,3,4)
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    //添加套餐与菜品的数据
    void saveMealWithDish(List<SetmealDish> setmealDishes);

    /**
     * 根据id将所有set_meal id的值删去
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id=#{id}")
    void deteleByMealId(Long id);

    /**
     * 根据套餐id返回套餐和菜品的关联关系
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> getByIdWithDish(Long id);
}
