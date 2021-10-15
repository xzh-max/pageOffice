package com.pageOfficeServer.util;

import java.io.File;

public class ProduceFileUtil {

    public static void main(String[] args){
        for(int i=100000;i<500000;i++){
            //获取当前文件路径
            String rootPath = "D:/pageOffice/target/pageOffice/template/"+i;
            File templatePath = new File(rootPath);
            if(!templatePath.exists()){
                templatePath.mkdir();
                //如果新模板不存就复制过来
                FileUtil.fileCopy_channel("D:/pageOffice/target/pageOffice/template/templateno-5/11.doc", rootPath+"/11.doc");
            }
        }
        System.out.print("success");
    }
}
