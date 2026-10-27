package com.sky.service;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品和对应的口味数据
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);



    /**
     * 菜品的批量删除
     * @param ids
     * @return
     */
    void deleteBatch(List<Long> ids);


    /**
     * 根据id查询出菜品数据和flavor进行回显
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);


    /**
     * 根据id动态修改菜品
     * @param dishDTO
     * @return
     */
    @AutoFill(OperationType.UPDATE)
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 菜品的起售停售
     * @param status id
     * @return
     */
    void startOrStop(int status,Long id);

    /**
     * 根据分类id查找当前菜品分类的菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);
}
