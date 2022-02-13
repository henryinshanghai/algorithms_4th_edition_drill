package com.henry.symbol_table_chapter_03.symbol_table_01.performanceDemo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 下载文件用的工具类
 */
public class downloadFileTool {

    /**
     * 从指定的URL下载文件并存储到指定的路径下
     * @param urlPath
     * @param storePath
     */
    public static void DownloadFileViaUrlToGivenPosition(String urlPath, String storePath) throws Exception {
        // 向URL发起请求
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        int code = conn.getResponseCode();
        if (code != 202 && code != 200) {
            throw new Exception("文件读取失败！");
        }

        // 从响应中获取文件流————作为输入流
        InputStream fis = new BufferedInputStream(conn.getInputStream());
        // 创建输出到指定路径的输出流
        File storeFile = new File(storePath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(storeFile));

        // 把输入流中的数据写入到输出流中
        // 1 创建一个缓冲区———— 字节数组（单位为M）
        byte[] buffer = new byte[1024 * 1024];

        // 2 读取输入流
        int read = 0;
        while ((read = fis.read(buffer)) != -1) {
            // 写入到输出流中
            bos.write(buffer, 0, read);
        }

        bos.flush();
        bos.close();
        fis.close();

    }


    public static void main(String[] args) throws Exception {
        // 准备URL与存储地址
        String urlPathStr = "https://algs4.cs.princeton.edu/31elementary/leipzig300K.txt";
        String storePath = "C:\\Users\\HenryInSH\\Desktop\\Temp\\leipzig300K.txt";

        // 这种方式要求storePath的具体文件已经存在，否则会出现“文件找不到”的异常
        DownloadFileViaUrlToGivenPosition(urlPathStr, storePath);
    }
}
