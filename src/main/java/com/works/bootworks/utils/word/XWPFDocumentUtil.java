package com.works.bootworks.utils.word;

import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date 2024-04-25
 * XWPFDocument 方式 【不推荐】
 * 占位符：${} word占位${object}有缺陷不能填充图片
 * 字体样式：可能需要代码编辑 XWPFRun run = para.createRun();run.setText(runText, 0); 不方便
 */
public class XWPFDocumentUtil {


    /**
     * 用一个docx文档作为模板，然后替换其中的内容，再写入目标文档中。
     *
     * @throws Exception
     */
    public static OutputStream templateWrite(String filePath, String outFilePath,
                                             Map<String, Object> params) throws Exception {
        InputStream is = new FileInputStream(filePath);
        XWPFDocument doc = new XWPFDocument(is);
        //替换段落里面的变量
        replaceInPara(doc, params);
        //替换表格里面的变量
        replaceInTable(doc, params);
        OutputStream os = new FileOutputStream(outFilePath);
        doc.write(os);
        close(os);
        close(is);
        return os;
    }

    /**
     * 替换段落里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     */
    private static void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        while (iterator.hasNext()) {
            para = iterator.next();
            replaceInPara(para, params);
        }
    }

    /**
     * 替换段落里面的变量
     *
     * @param para   要替换的段落
     * @param params 参数
     */
    private static void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
        List<XWPFRun> runs;
        Matcher matcher;
        String runText = "";
       int fontSize = 0;
        UnderlinePatterns underlinePatterns = null;
        if (matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            if (runs.size() > 0) {
                int j = runs.size();
                for (int i = 0; i < j; i++) {
                    XWPFRun run = runs.get(0);
                    if (fontSize == 0) {
                        fontSize = run.getFontSize();
                    }
                    if (underlinePatterns == null) {
                        underlinePatterns = run.getUnderline();
                    }
                    String i1 = run.toString();
                    runText += i1;
                    para.removeRun(0);
                }
            }
            matcher = matcher(runText);
            if (matcher.find()) {
                while ((matcher = matcher(runText)).find()) {
                    runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
                }
               //直接调用XWPFRun的setText()方法设置文本时，在底层会重新创建一个XWPFRun，把文本附加在当前文本后面，
               //所以我们不能直接设值，需要先删除当前run,然后再自己手动插入一个新的run。
               // para.insertNewRun(0).setText(runText);//新增的没有样式

                XWPFRun run = para.createRun();
                run.setText(runText, 0);
                run.setFontSize(fontSize);
                run.setUnderline(underlinePatterns);
                run.setFontFamily("仿宋");//字体
                run.setFontSize(16);//字体大小


                //run.setBold(true); //加粗
                //run.setColor("FF0000");
                //默认：宋体（wps）/等线（office2016） 5号 两端对齐 单倍间距
                //run.setBold(false);//加粗
                //run.setCapitalized(false);//我也不知道这个属性做啥的
                //run.setCharacterSpacing(5);//这个属性报错
                //run.setColor("BED4F1");//设置颜色--十六进制
                //run.setDoubleStrikethrough(false);//双删除线
                //run.setEmbossed(false);//浮雕字体----效果和印记（悬浮阴影）类似
                //run.setFontFamily("宋体");//字体
                //run.setFontFamily("华文新魏", FontCharRange.cs);//字体，范围----效果不详
                //run.setFontSize(14);//字体大小
                //run.setImprinted(false);//印迹（悬浮阴影）---效果和浮雕类似
                //run.setItalic(false);//斜体（字体倾斜）
                //run.setKerning(1);//字距调整----这个好像没有效果
                //run.setShadow(true);//阴影---稍微有点效果（阴影不明显）
                //run.setSmallCaps(true);//小型股------效果不清楚
                //run.setStrike(true);//单删除线（废弃）
                //run.setStrikeThrough(false);//单删除线（新的替换Strike）
                //run.setSubscript(VerticalAlign.SUBSCRIPT);//下标(吧当前这个run变成下标)---枚举
                //run.setTextPosition(20);//设置两行之间的行间距
                //run.setUnderline(UnderlinePatterns.DASH_LONG);//各种类型的下划线（枚举）
                //run0.addBreak();//类似换行的操作（html的  br标签）
                //run0.addTab();//tab键
                //run0.addCarriageReturn();//回车键
                //注意：addTab()和addCarriageReturn() 对setText()的使用先后顺序有关：比如先执行addTab,再写Text这是对当前这个Text的Table，反之是对下一个run的Text的Tab效果


            }
        }
    }


    /**
     * 替换表格里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     */
    private static void replaceInTable(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFTable> iterator = doc.getTablesIterator();
        XWPFTable table;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        while (iterator.hasNext()) {
            table = iterator.next();
            rows = table.getRows();
            for (XWPFTableRow row : rows) {
                cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    paras = cell.getParagraphs();
                    for (XWPFParagraph para : paras) {
                        replaceInPara(para, params);
                    }
                }
            }
        }
    }

    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    private static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }

    /**
     * 关闭输入流
     *
     * @param is
     */
    private static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    private static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
/*
    @SneakyThrows
    @Override
    public void printModelToPDF(TrustInstrumentBody body, HttpServletResponse httpServletResponse) {

// 小tips:支持换行数据展示效果
        String liftTypeJoin = "";
        String spmEquipIdJoin = "";
        for (int i = 0; i < 3; i++) {
            String test1 = "测试" + i + "型号";
            String test2 = "测试" + i + "KG";
            String test3 = "测试" + i + "m/s";
            String test4 = "SPM测试" + i;
            liftTypeJoin += org.apache.commons.lang3.StringUtils.join(test1, "  ", test2, "  ", test3, "\n");
            spmEquipIdJoin += org.apache.commons.lang3.StringUtils.join(test4, ",");
        }
        liftTypeJoin = liftTypeJoin.substring(0, liftTypeJoin.length() - 1);
        spmEquipIdJoin = spmEquipIdJoin.substring(0, spmEquipIdJoin.length() - 1);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", "编号123");//项目编号
        params.put("partnerName", "代理商甲");//经销代理
        params.put("installPartaName", "用户丙");//用户名称
        params.put("ladderType", liftTypeJoin);//类型
        params.put("spmEquipId", spmEquipIdJoin);//出厂编号
        params.put("warrantTime", DateUtils.format(new Date(), DateUtils.ISO8601_DATE_PATTERN));//委托时间为当前时间
        params.put("projectName", "用户丙");//项目名称

        // 模板word文件真实路径
        // 模板word文件真实路径
        String wordSrcPath = "E:\\批量下载文件夹\\安装委托书模版.docx";

        // 使用该办件编号作为文件名称
        String wordDestPath = "E:\\批量下载文件夹\\生成委托书模版.docx";
        POIUtil.templateWrite(wordSrcPath, wordDestPath, params);

}*/
