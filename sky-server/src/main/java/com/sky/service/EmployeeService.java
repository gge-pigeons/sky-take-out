package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     *
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);


    /**
     * 员工分页查询
     * 对于查询类Result中泛型一般才写，一般查询类才有data返回，其他的可以不用加。
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     *启用禁用员工账号
     * @param status  路径参数
     * @param id
     * @return
     */
    void startOrStop(Integer status, Long id);


    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    Employee getById(Long id);


    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
    void update(EmployeeDTO employeeDTO);
}
