package com.sky.service;

import com.sky.dto.SetmealDTO;

public interface SetmealService {

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    void saveWithDish(SetmealDTO setmealDTO);
}
