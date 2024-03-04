package com.works.bootworks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.works.bootworks.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserInfo2Mapper extends BaseMapper<UserInfo> {
    List<UserInfo> getByName(String username);
}
