package com.works.bootworks.controller;

import com.alibaba.excel.util.DateUtils;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.works.bootworks.entity.word.Student;
import com.works.bootworks.entity.word.StudentTable;
import com.works.bootworks.utils.word.XWPFDocumentUtil;
import com.works.bootworks.utils.word.XWPFTemplateUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;


/**
 * @date 2024-04-25
 * 动态赋值到word
 */
@RestController
@RequestMapping("/daoChu")
public class WordPoiController {

    /**
     * XWPFDocument 方式（不推荐）
     * @param response
     */
    @SneakyThrows
    @GetMapping("/exportWord")
    public void printModelToPDF(HttpServletResponse response) {
        // 小tips:支持换行数据展示效果
        String liftTypeJoin = "";
        String spmEquipIdJoin = "";
        for (int i = 0; i < 3; i++) {
            String test1 = "测试" + i + "型号\n";
            String test2 = "测试" + i + "KG\n";
            String test3 = "测试" + i + "m/s\n";
            String test4 = "SPM测试" + i + "\n";
            liftTypeJoin += StringUtils.join(test1, "  ", test2, "  ", test3, "\n");
            spmEquipIdJoin += StringUtils.join(test4, ",");
        }
        liftTypeJoin = liftTypeJoin.substring(0, liftTypeJoin.length() - 1);
        spmEquipIdJoin = spmEquipIdJoin.substring(0, spmEquipIdJoin.length() - 1);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", "编号123");//项目编号
        params.put("partnerName", "代理商甲");//经销代理
        params.put("installPartaName", "用户丙");//用户名称
        params.put("ladderType", liftTypeJoin);//类型
        params.put("spmEquipId", spmEquipIdJoin);//出厂编号
        params.put("warrantTime", DateUtils.format(new Date(), "yyyy-MM-dd"));//委托时间为当前时间
        params.put("projectName", "用户丙");//项目名称
        // 模板word文件真实路径
        String wordSrcPath = "C:\\ccideawork\\boot-works\\word\\入职材料模板Doc.docx";
        // 使用该办件编号作为文件名称
        String wordDestPath = "C:\\入职材料1.docx";
        XWPFDocumentUtil.templateWrite(wordSrcPath, wordDestPath, params);
    }



    /**
     * XWPFTemplate 方式（推荐）
     * 固定字段 行数
     * @param response
     */
    @GetMapping("genera")
    public void genera(HttpServletResponse response){
        //1.组装数据
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", "编号123");//项目编号
        params.put("partnerName", "代理商甲");//经销代理
        params.put("installPartaName", "用户丙");//用户名称

        params.put("warrantTime", DateUtils.format(new Date(), "yyyy-MM-dd"));//委托时间为当前时间
        params.put("projectName", "用户丙");//项目名称
        //添加图片
        //params.put("image", Pictures.ofUrl("http://deepoove.com/images/icecream.png").size(100, 100).create());

        //2.获取根目录，创建模板文件
        //String path = copyTempFile("word\\入职材料模板.docx");
        String path = "C:\\ccideawork\\boot-works\\word\\入职材料模板.docx";

        String fileName = System.currentTimeMillis() + ".docx";
        String tmpPath = "C:\\" + fileName; //临时下载的地址

        try {
            //3.将模板文件写入到根目录
            //4.编译模板，渲染数据
            XWPFTemplate template = XWPFTemplate.compile(path).render(params);

            //5.写入到指定目录位置
            FileOutputStream fos = new FileOutputStream(tmpPath);
            template.write(fos);
            fos.flush();
            fos.close();
            template.close();
            //6.提供前端下载
            XWPFTemplateUtil.down(response, tmpPath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //7.删除临时文件
            File file = new File(tmpPath);
            file.delete();
            //模板file【之前是拷贝一份临时文件作为模板，所以删除。现未拷贝所以模板不删除】
            //File copyFile = new File(path);
            //copyFile.delete();
        }
    }


    /**
     * 表格，循环多行
     * 注意：
     *  模板（docx2.docx）中有两个list，这两个list需要置于循环行的上一行。
     *  循环行设置要循环的标签和内容，注意此时的标签应该使用 []
     * @param response
     *
     */
    @GetMapping("dynamicTable")
    public void dynamicTable(HttpServletResponse response) {
        //1.组装数据
        StudentTable table = assertData();
        //2.获取根目录，创建模板文件
        //String path = copyTempFile("word/2.docx");
        String path = "C:\\ccideawork\\boot-works\\word\\docx2.docx";

        //3.获取临时文件
        String fileName = System.currentTimeMillis() + ".docx";
        String tmpPath = "C:\\" + fileName;
        try {
            //4.编译模板，渲染数据  LoopRowTableRenderPolicy 是一个特定场景的插件，根据集合数据循环表格行。
            LoopRowTableRenderPolicy hackLoopTableRenderPolicy = new LoopRowTableRenderPolicy();
            Configure config =
                    Configure.builder()
                            .bind("studentList", hackLoopTableRenderPolicy)
                            .bind("studentList1", hackLoopTableRenderPolicy).build();
            XWPFTemplate template = XWPFTemplate.compile(path, config).render(table);
            //5.写入到指定目录位置
            FileOutputStream fos = new FileOutputStream(tmpPath);
            template.write(fos);
            fos.flush();
            fos.close();
            template.close();
            //6.提供下载
            XWPFTemplateUtil.down(response, tmpPath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //7.删除临时文件
            File file = new File(tmpPath);
            file.delete();
//            File copyFile = new File(path);
//            copyFile.delete();
        }
    }
    private StudentTable assertData() {
        StudentTable table = new StudentTable();
        table.setTitle("我是标题");
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setName("张三");
        student.setAge("18");
        studentList.add(student);
        Student student1 = new Student();
        student1.setName("李四");
        student1.setAge("20");
        studentList.add(student1);
        Student student2 = new Student();
        student2.setName("王五");
        student2.setAge("21");
        studentList.add(student2);
        Student student3 = new Student();
        student3.setName("马六");
        student3.setAge("19");
        studentList.add(student3);
        table.setStudentList(studentList);
        table.setStudentList1(studentList);
        return table;
    }




}

