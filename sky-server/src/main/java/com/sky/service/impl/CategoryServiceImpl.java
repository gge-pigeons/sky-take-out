package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        //设置修改时间、修改人
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);
    }

    /**
     * 根据分类的type来进行查询，这里不用PageResult因为不需要返回总页数，不需要total，只需要records，因此自己定义一个泛型List<Category>
     * @param type
     * @return
     */
    @Override
    public List<Category> list(Integer type) {
        List<Category> list = categoryMapper.list(type);
        return list;
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @Override
    public void deleteById(Long id) {
        //查询当前分类是否关联了菜品，如果关联了就抛出业务异常
        Integer count = dishMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        //查询当前分类是否关联了套餐，如果关联了就抛出业务异常
        count = setmealMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        //根据id删除分类
        categoryMapper.delete(id);
    }

    /**
     * 新增一个分类
     * @param categoryDTO
     * @return
     */
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        //1.先将DTO转换为实体类
        BeanUtils.copyProperties(categoryDTO,category);
        //2.接着将其他属性补齐
        //默认禁用
        category.setStatus(0);
        //createTime和updateTime
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        //创建人id和修改人id
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.save(category);
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        long total = page.getTotal();
        List<Category> records = page.getResult();
        return new PageResult(total,records);
    }


    /**
     * 启用禁用菜品
     * @param status
     * @param id
     * @return
     */
    @Override
    public void startOrStop(int status, Long id) {
        //原本应该是直接 update category set status=? where id=?
        //现在为了扩展性，直接传递一个category，方便后续update其他
        Category category = Category.builder()
                        .id(id)
                        .status(status)
                        .build();
        categoryMapper.update(category);
    }
}
