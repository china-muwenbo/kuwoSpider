package com.dylan.kuwoSpider.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveUtils {
    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void save(String fileName, String content) {
        File file=new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        content=content+"\n";
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(fileName, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        save("test.txt","a");
        save("test.txt","b");
        save("test.txt","c");
    }
}
