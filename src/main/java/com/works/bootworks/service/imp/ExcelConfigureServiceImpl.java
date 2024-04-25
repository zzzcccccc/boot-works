package com.works.bootworks.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.works.bootworks.entity.excel.ExcelConfigureEntity;
import com.works.bootworks.mapper.ExcelConfigureMapper;
import com.works.bootworks.service.ExcelConfigureService;
import org.springframework.stereotype.Service;

@Service
public class ExcelConfigureServiceImpl extends ServiceImpl<ExcelConfigureMapper, ExcelConfigureEntity>
        implements ExcelConfigureService {
}
