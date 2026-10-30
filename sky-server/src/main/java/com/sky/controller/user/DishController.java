package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId){
        //构造redis中的key，规则：dish_分类id
        String key = "dish_"+categoryId;
        //查询redis中是否存在菜品数据
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        //如果存在，直接返回，无需查询数据库
        if(list!=null&&list.size()>0){
            return Result.success(list);
        }
        //如果不存在，查询数据库，将查询到的数据放到redis中
        List<Dish> dishes = dishService.list(categoryId);
        List<DishVO> dishVOS = new ArrayList<>();
        dishes.forEach(dish -> {
            DishVO dishVO = dishService.getByIdWithFlavor(dish.getId());
            dishVOS.add(dishVO);
        });
        //将这个数据放在redis中
        redisTemplate.opsForValue().set(key,dishVOS);
        return Result.success(dishVOS);
    }
}
