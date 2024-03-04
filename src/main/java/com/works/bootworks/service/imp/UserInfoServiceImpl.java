package com.works.bootworks.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.works.bootworks.entity.ExcelConfigureEntity;
import com.works.bootworks.entity.UserInfo;
import com.works.bootworks.mapper.ExcelConfigureMapper;
import com.works.bootworks.mapper.UserInfoMapper;
import com.works.bootworks.service.UserInfoService;
import com.works.bootworks.utils.ExcelUtil;
import com.works.bootworks.utils.SpringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {
    @Autowired
    private ExcelConfigureMapper excelConfigureMapper;

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

        //获取该模板数据
        ExcelConfigureEntity excelConfigureEntity = excelConfigureMapper.selectById(3);
        //获取当前上下文环境，spring容器
        //WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        //加载class类
        Class<?> clazz = null;
        try {
            clazz = Class.forName(excelConfigureEntity.getServiceName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //获得方法
        Method m = null;
        try {
            m = clazz.getMethod(excelConfigureEntity.getServiceMethodName());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        //执行方法,获得数据
        String[] arr = excelConfigureEntity.getServiceName().split("\\.");
        List<Object> listMap  = null;
        try {
            //首字母转小写
            String s = arr[arr.length - 1];
            s = (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
            Object bean = SpringUtils.getBean(s);
            listMap = (List<Object>) m.invoke(bean);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        //实体类反射
        Class<?> clazz2 = null;
        try {
             clazz2 = Class.forName(excelConfigureEntity.getEntityName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //  List<UserInfo> userInfos = this.baseMapper.selectList(null);
        //easyPoi，也可以根据模板自定义导出等……
        ExcelUtil.exportExcel(response, excelConfigureEntity.getFileName(), listMap, clazz2);

    }
}
