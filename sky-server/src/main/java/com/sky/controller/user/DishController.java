package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId){
        List<Dish> dishes = dishService.list(categoryId);
        List<DishVO> dishVOS = new ArrayList<>();
        dishes.forEach(dish -> {
            DishVO dishVO = dishService.getByIdWithFlavor(dish.getId());
            dishVOS.add(dishVO);
        });
        return Result.success(dishVOS);
    }
}
