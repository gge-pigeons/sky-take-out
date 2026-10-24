package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * @return
     */
    @Insert("Insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "values(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void save(Category category);


    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);


    /**
     * 启用禁用菜品
     * @param  category
     * @return
     */
    void update(Category category);


    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @Delete("delete from category where id = #{id}")
    void delete(Long id);



    /**
     * 根据分类的type来进行查询，这里不用PageResult因为不需要返回总页数，不需要total，只需要records，因此自己定义一个泛型List<Category>
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
