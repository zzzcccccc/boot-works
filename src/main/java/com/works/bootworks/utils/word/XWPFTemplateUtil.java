package com.works.bootworks.utils.word;


import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @date 2024-04-25
 * XWPFTemplate 方式  【推荐】
 * 占位符: {{}} word占位用{{object}}比较完美可以填充图片
 * 字体样式：在word文件中设计好就行，会自动替换值，样式保留
 */
public class XWPFTemplateUtil {

    /**
     * 将项目中的模板文件拷贝到根目录下
     * @return
     */
    private String copyTempFile(String templeFilePath) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templeFilePath);
        String tempFileName = System.getProperty("user.dir") + "\\word\\入职材料模板.docx";
        File tempFile = new File(tempFileName);
        try {
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile.getPath();
    }

    public static void down(HttpServletResponse response, String filePath, String realFileName) {
        String percentEncodedFileName = null;
        try {
            percentEncodedFileName = percentEncode(realFileName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=").append(percentEncodedFileName).append(";").append("filename*=").append("utf-8''").append(percentEncodedFileName);

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue.toString());
        response.setHeader("download-filename", percentEncodedFileName);
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
             // 输出流
             BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());) {
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = bis.read(buff)) > 0) {
                bos.write(buff, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 百分号编码工具方法
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }
}
