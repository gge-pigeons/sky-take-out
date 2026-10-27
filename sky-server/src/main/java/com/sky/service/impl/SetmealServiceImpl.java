package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public void startOrStop(Integer status, Long id) {
        //如果套餐中有菜品处于禁售，那么套餐就不能起售
        if(status==StatusConstant.ENABLE){
            List<Dish> dishList = dishMapper.getByMealId(id);
            if(dishList!=null &&dishList.size()>0){
                dishList.forEach(dish->{
                    if(StatusConstant.DISABLE==dish.getStatus()){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);

    }

    @Override
    public void update(SetmealDTO setmealDTO) {
        //首先将数据进行回显，这一步根据套餐id查询已经完成了
        //进行修改操作
        //获取套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //将套餐关联的菜品信息全部删除
        setmealDishMapper.deteleByMealId(setmealDTO.getId());
        //重新插入菜品信息,这里还需要套餐的id。
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealDTO.getId());
        });
        setmealDishMapper.saveMealWithDish(setmealDishes);
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //然后进行更改
        setmealMapper.update(setmeal);
    }

    @Override
    public SetmealVO getById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealdishes= setmealDishMapper.getByIdWithDish(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealdishes);
        return setmealVO;
    }

    @Transactional
    public void deleteBatch(List<Long> ids) {
        //先查询要删除的套餐是否在售，如果有则抛出异常
        ids.forEach(id->{
            Setmeal setmeal = setmealMapper.getById(id);
            if(StatusConstant.ENABLE==setmeal.getStatus()){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        ids.forEach(id->{
            //将套餐从套餐表中删除
            setmealMapper.delete(id);
            //然后要将套餐关联的菜品全部删除
            setmealDishMapper.deteleByMealId(id);
        });
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        //先创建PageHelper  获取要查询的页数和每页的信息数量
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        //查询
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //先添加套餐的基本信息
        setmealMapper.insert(setmeal);
        //紧接着将套餐的信息与菜品相关联,即向数据库表setmeal_dish中添加数据
        //需要一个SetmealDish对象，这里面很多数据都是从之前传的DTO中可以获取
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
        });
        //将setmealDishes用for循环包裹，此刻只需要知道套餐id就可以直接添加
        setmealDishMapper.saveMealWithDish(setmealDishes);
    }
}
