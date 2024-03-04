package com.works.bootworks.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类模拟
 */
@Data
@TableName("user_s")
public class UserInfo implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)//指定自增策略
    @ExcelProperty("序号")
    private int id;

    @ExcelProperty("username")
    private String username;

    @ExcelProperty("password")
    private String password;

    @ExcelProperty("role")
    private String role;

    @ExcelProperty("badge")
    private Integer badge;
}
