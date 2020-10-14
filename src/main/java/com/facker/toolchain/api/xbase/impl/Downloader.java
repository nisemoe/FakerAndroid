package com.facker.toolchain.api.xbase.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class Downloader {

    //https://www.greplay.com/wp-json/apiv2/list?from=index&page=2
    //https://www.greplay.com/wp-json/apiv2/rank?sort=1&page=2
    //https://www.greplay.com/wp-json/apiv2/detail?id=26123
    //https://www.greplay.com/wp-json/apiv2/cat?term=-2&page=2
    public static void main(String[] args) {
        for (int i = 0; i < 40; i++) {
            String page = sendGet("https://www.greplay.com/wp-json/apiv2/list", "from=index&page=" + (i + 1));
            System.out.println("page  = " + page);
            try {
                JSONObject obj = new JSONObject(page);
                JSONArray idsJSON = obj.getJSONArray("body");
                for (int j = 0; j < idsJSON.length(); j++) {
                    JSONObject idObj = idsJSON.getJSONObject(j);
                    int id = idObj.getInt("id");
                    System.out.println("------------id :" + id + "------------------");
                    parseDeatai(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void parseDeatai(int id) {
        String detailUrl = "https://www.greplay.com/wp-json/apiv2/detail?id=" + id;

        String detailStr = sendGet(detailUrl, "");

        System.out.println("detail " + detailStr);

        try {
            JSONObject jsonObjectDetail = new JSONObject(detailStr).getJSONObject("body");
            String title = jsonObjectDetail.getJSONObject("basic").getString("title");
            String download = jsonObjectDetail.getJSONObject("download").getString("file");
            download.replace("https", "http");
            String pkg = jsonObjectDetail.getJSONObject("download").getString("file_package_name");
            //String fileName = title+"_"+pkg+".apk";

            downLoadFromUrl(download, title + "_" + pkg + ".apk", "D:\\apker");
//            String logoUrl = jsonObjectDetail.getJSONObject("basic").getString("logo");
//            JSONArray atlasJSon = jsonObjectDetail.getJSONArray("atlas");
//            List<String> urls = new ArrayList<>();
//            for (int i = 0;i<atlasJSon.length();i++) {
//                String url = atlasJSon.getString(i);
//
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static String downLoadFromUrl(String urlStr, String fileName, String savePath) {
        try {

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            // 得到输入流
            InputStream inputStream = conn.getInputStream();
            // 获取自己数组
            byte[] getData = readInputStream(inputStream);

            // 文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            // System.out.println("info:"+url+" download success");
            return saveDir + File.separator + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
