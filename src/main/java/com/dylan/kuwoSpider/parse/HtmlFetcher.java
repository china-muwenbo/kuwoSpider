package com.dylan.kuwoSpider.parse;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class HtmlFetcher {
    /**
     * @Description: 通过代理获取网页内容
     * @Param:  * @param href
     * @return: java.lang.String
     */
    public static String getDocByProxy(String href) {
        String ip = "180.118.86.99";
        int port = 9000;
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
            URL url = new URL(href);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection(proxy);
            urlcon.setConnectTimeout(15000);
            urlcon.setReadTimeout(15000);
            urlcon.connect();         //获取连接
            InputStream is = urlcon.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuffer bs = new StringBuffer();
            String l = null;
            while ((l = buffer.readLine()) != null) {
                bs.append(l);
            }
            System.out.println(bs.toString());
            return bs.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: 获取的url的内容,没有代理
     * @Param: [url]
     * @return: java.lang.String
     */
    public static String fetch(String url) {
        try {
            Connection.Response response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                    .timeout(3000).execute();
            return response.statusCode() / 100 == 2 ? response.body() : null;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * @Description: 获取的url的内容,有代理
     * @Param: [url]
     * @return: java.lang.String
     */
    public static String fetchProxy(String url) {
        return getDocByProxy(url);
    }
    /**
     * @Description: 获取的url重定向的地址
     * @Param: [url]
     * @return: java.lang.String
     */
    public static String fetchMusic(String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().followRedirects(false).build();
        Request request = new Request.Builder()
                .addHeader("userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.header("Location");
    }
}
