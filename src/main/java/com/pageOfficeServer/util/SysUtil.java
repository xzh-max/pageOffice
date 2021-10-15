package com.pageOfficeServer.util;



public class SysUtil {

    //判断是不是linux系统，true返回是，false返回否
    public static boolean IsLinux(){
        String osName = System.getProperty("os.name");
        if(!osName.toLowerCase().startsWith("win")){
            return true;
        }
        return false;
    }
}
