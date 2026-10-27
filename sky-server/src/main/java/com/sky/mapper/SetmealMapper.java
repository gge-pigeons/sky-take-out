package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
public interface SetmealMapper {



    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);


    /**
     * 新建套餐的基本信息
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);


    /**
     * 分页查询
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 通过套餐id获取套餐
     * @param id
     * @return
     */
    @Select("select * from setmeal where id=#{id}")
    Setmeal getById(Long id);


    /**
     * 将套餐表中数据根据id删除
     * @param id
     */
    @Delete("delete from setmeal where id=#{id}")
    void delete(Long id);


    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
