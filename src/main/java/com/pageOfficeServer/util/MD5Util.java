package com.pageOfficeServer.util;

import java.security.MessageDigest;

public class MD5Util {
     /***
       * MD5加码 生成32位md5码
       */
              public static String string2MD5(String inStr){
    MessageDigest md5 = null;
    try{
      md5 = MessageDigest.getInstance("MD5");
    }catch (Exception e){
      System.out.println(e.toString());
      e.printStackTrace();
      return "";
    }
    char[] charArray = inStr.toCharArray();
    byte[] byteArray = new byte[charArray.length];

    for (int i = 0; i < charArray.length; i++)
      byteArray[i] = (byte) charArray[i];
    byte[] md5Bytes = md5.digest(byteArray);
    StringBuffer hexValue = new StringBuffer();
    for (int i = 0; i < md5Bytes.length; i++){
      int val = ((int) md5Bytes[i]) & 0xff;
      if (val < 16)
        hexValue.append("0");
      hexValue.append(Integer.toHexString(val));
    }
    return hexValue.toString();

  }

              /**
       * 加密解密算法 执行一次加密，两次解密
       */
              public static String convertMD5(String inStr){

    char[] a = inStr.toCharArray();
    for (int i = 0; i < a.length; i++){
      a[i] = (char) (a[i] ^ 't');
    }
    String s = new String(a);
    return s;

  }

    // 测试主函数
    public static void main(String args[]) {
    String s = new String("filename=WS-2019-XDD0-YZ-000209.docx&type=contract");
//    System.out.println("原始：" + s);
//    System.out.println("MD5后：" + string2MD5(s));
//    System.out.println("解密的：" + convertMD5(convertMD5(s)));
     System.out.println("原始：" + s);
     System.out.println("MD5后：" + string2MD5(s));
    System.out.println("加密的：" + convertMD5(s));
     System.out.println("解密的：" + convertMD5(convertMD5(s)));

//    String fileId="fileName=WS-2019-XDD0-YZ-000209.docx&type=contract";
//    String[] fs=fileId.split("&type");
//    String fileName=fs[0].split("=")[1];
//    System.out.print(fileName);
//    String tupe=fs[1].split("=")[1];
//        System.out.print(fs[1].split("=")[0]);
//        String fileId=MD5Util.string2MD5(new String("fileName=WS-2019-XDD0-YZ-000209.docx&type=contract"));
//        System.out.print(fileId+"\n");
//        String filepath="http://10.11.50.11/pageOffice/download.do?fileId="+fileId;
//        fileId=filepath.split("fileId=")[1];
//        System.out.print(fileId+"\n");
//        String path=MD5Util.string2MD5(new String("fileName=WS-2019-XDD0-YZ-000209.docx&type=contract"));
//
//        String file=convertMD5(convertMD5(path));
//        System.out.print(file+"\n");
//        filepath=filepath.split("fileId=")[0]+file;
//        System.out.print(filepath);
  }

}
