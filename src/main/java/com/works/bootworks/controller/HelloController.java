package com.works.bootworks.controller;


import com.works.bootworks.entity.UserInfo;
import com.works.bootworks.service.UserInfoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 统一导出接口
 */
@RestController
@RequestMapping("/daoChu")
public class HelloController {


    @Autowired
    private UserInfoService userInfoService;


    /**
     * 导出excel  ----使用反射  统一导出接口
     * @param response
     * @param personnelDto  其他条件
     * @return
     */
    @PostMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) {
        userInfoService.exportExcel(response);
    }




    @GetMapping("/getAll")
    public  List<UserInfo> getAll(){
        List<UserInfo> list = userInfoService.getAll();
        return list;
    }

}
