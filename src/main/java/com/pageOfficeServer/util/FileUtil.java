package com.pageOfficeServer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Random;

public class FileUtil {

    public static void main(String[] args) {
        // This is the path where the file's name you want to take.
//        String path = "D:\\pageOffice\\target\\pageOffice\\contract\\01.doc";
////        deleteFileByNo("001.pdf",path);
////        System.out.print(getFilePath(path));
//        String[] arrays=path.split(".doc");
//        System.out.print(arrays.length);
        String str ="232ljsfsf.sdfl23.ljsdfsdfsdfss.23423.sdfsdfsfd";
        //获得第一个点的位置
//        int index=str.indexOf(".");
//        System.out.println(index);
//        //根据第一个点的位置 获得第二个点的位置
//        index=str.indexOf(".", index+1);
        //根据第二个点的位置，截取 字符串。得到结果 result
        String[] result=str.split(".");
        //输出结果
//        System.out.println(result[2]);
        System.out.print(getCharAndNumr(20));
    }

    public static String getFileNameBYpath(String fileName){
        if(fileName.endsWith(".doc")||fileName.endsWith(".docx")){
            String names=fileName.substring(fileName.lastIndexOf("/")+1);
            return names.split("\\."+"doc")[0];
        }else {
            return null;
        }
    }

    public static String getFilePath(String path) {
        File file = new File(path);
        File[] array = file.listFiles();

        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                return array[i].getPath();
            }
        }
        return null;
    }

    public static boolean fileExit(String fileName,String path) {
        try {
            File folder = new File(path);
            File[] files = folder.listFiles();
            System.out.print("合同文件路径"+path);
            System.out.print("合同文件名称"+fileName);
            for(File file:files){
                if(file.getName().equals(fileName)){
                   return true;
                }
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    public static boolean deleteFileByNo(String fileName,String path) {
        try {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for(File file:files){
                if(file.getName().equals(fileName)){
                    file.delete();
                }
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static String getFileName(String path) {
        File file = new File(path);
        File[] array = file.listFiles();

        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                return array[i].getName();
            }
        }
        return null;
    }

    public static void fileCopy_channel(String fromPath,String toPath) {
        FileChannel input = null;
        FileChannel output = null;

        try {
            input = new FileInputStream(fromPath).getChannel();
            output = new FileOutputStream(toPath).getChannel();
            /**
             * Transfers bytes into this channel's file from the given readable byte channel.
             *  @param  src
                 *         The source channel
                 *
                 * @param  position
                 *         The position within the file at which the transfer is to begin;
                 *         must be non-negative
                 *
                 * @param  count
                 *         The maximum number of bytes to be transferred; must be
                 *         non-negative
             */
            output.transferFrom(input, 0, input.size());
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getCharAndNumr(int length) {
        String val = "";
         Random random = new Random();
         for (int i = 0; i < length; i++) {
          // 输出字母还是数字
          String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
                    // 字符串
                    if ("char".equalsIgnoreCase(charOrNum)) {
             // 取得大写字母还是小写字母
             int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
             val += (char) (choice + random.nextInt(26));
                    } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
             val += String.valueOf(random.nextInt(10));
                    }
         }
         return val;
    }

}
