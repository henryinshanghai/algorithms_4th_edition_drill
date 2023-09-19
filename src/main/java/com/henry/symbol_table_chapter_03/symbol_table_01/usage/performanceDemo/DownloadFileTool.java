package com.henry.symbol_table_chapter_03.symbol_table_01.usage.performanceDemo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载文件用的工具类
 */
public class DownloadFileTool {

    /**
     * 从指定的URL下载文件并存储到指定的路径下
     * @param urlPath
     * @param storePath
     */
    public static void downloadFileViaUrlToGivenPosition(String urlPath, String storePath) throws Exception {
        // 向URL发起请求
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        int code = conn.getResponseCode();
        if (code != 202 && code != 200) {
            throw new Exception("文件读取失败！");
        }

        // 从响应中获取"文件流"————作为"输入流"
        InputStream fis = new BufferedInputStream(conn.getInputStream());
        // 创建 "输出到指定路径的输出流"
        File storeFile = new File(storePath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(storeFile));

        /* 把输入流中的数据 "写入到输出流中" */
        // #1 创建一个缓冲区———— 字节数组（单位为M）
        byte[] buffer = new byte[1024 * 1024];

        // #2 读取输入流
        int read = 0;
        while ((read = fis.read(buffer)) != -1) {
            // #3 写入到输出流中
            bos.write(buffer, 0, read);
        }

        // 清空缓冲区
        bos.flush();
        // 关闭资源
        bos.close();
        fis.close();
    }


    public static void main(String[] args) throws Exception {
        // 准备URL与存储地址
        // 🐖 URL中的斜杠是正斜杠、文件路径中的斜杠是反斜杠
        String urlPathStr = "https://algs4.cs.princeton.edu/31elementary/leipzig300K.txt";
        String storePath = "E:\\development_project\\algorithms_4th_edition_drill\\src\\main\\java\\com\\henry\\symbol_table_chapter_03\\symbol_table_01\\performanceDemo\\leipzig300k.txt";

        // 🐖 这种方式要求storePath的具体文件已经存在，否则会出现“文件找不到”的异常
        downloadFileViaUrlToGivenPosition(urlPathStr, storePath);
    }
}
