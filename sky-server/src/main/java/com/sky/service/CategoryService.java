package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {



    /**
     * 新增一个分类
     * @param categoryDTO
     * @return
     */
    void save(CategoryDTO categoryDTO);


    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);


    /**
     * 启用禁用菜品
     * @param status
     * @param id
     * @return
     */
    void startOrStop(int status, Long id);


    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    void deleteById(Long id);



    /**
     * 根据分类的type来进行查询，这里不用PageResult因为不需要返回总页数，不需要total，只需要records，因此自己定义一个泛型List<Category>
     * @param type
     * @return
     */
    List<Category> list(Integer type);


    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    void update(CategoryDTO categoryDTO);
}
