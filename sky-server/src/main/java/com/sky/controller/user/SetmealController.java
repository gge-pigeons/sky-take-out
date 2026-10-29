package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

@RestController("userSetMealController")
@RequestMapping("/user/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishMapper dishMapper;


    @GetMapping("/list")
    public Result<List<Setmeal>> list(Long categoryId){
        List<Setmeal> setmealList = setmealService.list(categoryId);
        return Result.success(setmealList);
    }

    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> getByIdWithDish(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        //获取套餐对应的菜品的关联关系
        List<SetmealDish> setmealDishes = setmealVO.getSetmealDishes();
        //每一样菜品需要返回的信息
        List<DishItemVO> dishItemVOS = new ArrayList<>();
        setmealDishes.forEach(setmealDish -> {
            DishItemVO dishItemVO = new DishItemVO();
            BeanUtils.copyProperties(setmealDish,dishItemVO);
            //根据菜品名称获取菜品
            Dish dish = dishMapper.getByName(setmealDish.getName());
            dishItemVO.setImage(dish.getImage());
            dishItemVO.setDescription(dish.getDescription());
            dishItemVOS.add(dishItemVO);
        });
        return Result.success(dishItemVOS);
    }
}
