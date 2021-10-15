package com.pageOfficeServer.util;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Bookmark;
import org.apache.poi.hwpf.usermodel.Bookmarks;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DocBookMarkProcessUtil {
    private static void replaceDoc(Map<String, String> dataMap, HWPFDocument doc) throws IOException {
        if (doc != null) {
            Bookmarks bookmarks = doc.getBookmarks();
            int count = bookmarks.getBookmarksCount();
            for (int i = 0; i < count; i++) {
                Bookmark bookmark = null;
                try {
                    bookmark = bookmarks.getBookmark(i);
                } catch (Exception e) {
                    System.err.println(e.getCause());
                    continue;
                }
                if (bookmark.getName() != null && bookmark.getName().startsWith("PO_")) {
                    String bookmarkName = bookmark.getName().trim().toUpperCase();
                    String data = dataMap.get(bookmarkName).trim();
                    Range range = new Range(bookmark.getStart(), bookmark.getEnd(), doc);
                    range.insertBefore(data);
                    System.out.println(bookmark.getStart()+"："+bookmark.getEnd());
                }
            }
        }
    }

    public static Map<String, String> getDataMap(String templatePath) {
        Map<String, String> dataMap = new HashMap<>();
        InputStream is;
        HWPFDocument doc = null;
        try {
            is = new FileInputStream(templatePath);
            doc = new HWPFDocument(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc != null) {
            for (int i = 0; i < doc.getBookmarks().getBookmarksCount(); i++) {
                final Bookmark bookmark = doc.getBookmarks().getBookmark(i);
                if (bookmark.getName() != null && bookmark.getName().startsWith("PO_")) {
                    final Range range = new Range(bookmark.getStart(), bookmark.getEnd(), doc);
                    String bookmarkName = bookmark.getName().trim().toUpperCase();
                    dataMap.put(bookmarkName, range.text());
                }
            }
        }
        return dataMap;
    }

    public static void main(String[] args) {
        String templatePath = "D:\\poitest\\test.doc";
        String outPath = "D:\\poitest\\result.doc";
        File outfile = new File(outPath);
        Map<String, String> dataMap = getDataMap(templatePath);
        HWPFDocument doc = null;
        InputStream is = null;
        try {
            is = new FileInputStream(outfile);
            doc = new HWPFDocument(is);
            replaceDoc(dataMap, doc);

            OutputStream os = new FileOutputStream(outPath);
            // 把doc输出到输出流中
            doc.write(os);
            closeStream(os);
            closeStream(is);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("game over");
    }

    /**
     * 关闭输入流
     *
     * @param is
     */
    private static void closeStream(InputStream is) {
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
    private static void closeStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
