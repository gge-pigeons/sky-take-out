package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增一个分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类");
        categoryService.save(categoryDTO);
        return Result.success();
    }


    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分类分页查询{}",categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用菜品
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable int status, Long id){
        log.info("启用禁用分类{},{}",status,id);
        categoryService.startOrStop(status,id);
        return Result.success();
    }


    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public Result deleteById(Long id){
        log.info("根据id删除分类");
        categoryService.deleteById(id);
        return Result.success();
    }


    /**
     * 根据分类的type来进行查询，这里不用PageResult因为不需要返回总页数，不需要total，只需要records，因此自己定义一个泛型List<Category>
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }
}
