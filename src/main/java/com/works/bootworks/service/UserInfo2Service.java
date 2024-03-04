package com.works.bootworks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.works.bootworks.entity.UserInfo;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


public interface UserInfo2Service extends IService<UserInfo> {

    List<UserInfo> getAll();

    List<UserInfo> getByName(String username);

    void exportExcel(HttpServletResponse response);
}
