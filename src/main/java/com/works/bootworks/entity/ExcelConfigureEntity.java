package com.works.bootworks.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一导出
 * @date 2024-03-04
 */
@Data
@TableName("excel_configure")
public class ExcelConfigureEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    /**
     * 导出的时候，只需要根据该id查询模板即可
     */
    private Integer id;

    /**
     * Service名称，Service的路径，反射使用（我这里使用的是Controller）
     */
    private String serviceName;

    /**
     * Service方法名称，反射使用（Controller方法名）
     */
    private String serviceMethodName;

    private String entityName;

    /**
     * 模板地址
     */
    private String templateUrl;

    /**
     * 导出的文件名称
     */
    private String fileName;

    /**
     * 导出的路径
     */
    private String savePath;


}
