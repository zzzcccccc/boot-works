package com.works.bootworks.service.imp;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.works.bootworks.entity.UserInfo;
import com.works.bootworks.mapper.UserInfo2Mapper;
import com.works.bootworks.service.UserInfo2Service;
import com.works.bootworks.utils.excel.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfo2ServiceImpl extends ServiceImpl<UserInfo2Mapper, UserInfo>
        implements UserInfo2Service {

    @Override
    public List<UserInfo> getAll() {
        return this.baseMapper.selectList(null);
    }

    @Override
    public List<UserInfo> getByName(String username) {
        return this.baseMapper.getByName(username);
    }

    @Override
    public void exportExcel(HttpServletResponse response) {
        List<UserInfo> userInfos = this.baseMapper.selectList(null);
        ExcelUtil.exportExcel(response, "user2", userInfos, UserInfo.class);
    }
}
