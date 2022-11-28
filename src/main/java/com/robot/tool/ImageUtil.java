package com.robot.tool;


import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * ImageUtil
 * TODO
 *
 * @author :linwei
 * @version :1.0
 * @date :2021/6/23 下午12:09
 */
public class ImageUtil {
    /**
     * 将二进制数组转换为图片
     *
     * @param data 二进制数组
     * @param path 转化图片后的地址
     * @throws Exception
     */
    public static void byte2image(byte[] data, String path) throws Exception {
        if (data.length < 3 || path.equals("")) return;
        try {
            for (int i = 0; i < data.length; ++i) {
                if (data[i] < 0) {//调整异常数据
                    data[i] += 256;
                }
            }
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            System.out.println("图片转换成功： " + path);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("图片转换异常: " + ex);
        }
    }

    /**
     * 将图片转换成为二进制数组
     *
     * @param path 图片路径
     * @return
     */
    public static byte[] image2byte(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

}
