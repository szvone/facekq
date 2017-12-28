package cn.szvone.util;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Http请求工具类
 * @author snowfigure
 * @since 2014-8-24 13:30:56
 * @version v1.0.1
 */
public class HttpRequestUtil {
    static boolean proxySet = false;
    static String proxyHost = "127.0.0.1";
    static int proxyPort = 8087;
    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    /**
     * 编码
     * @param source
     * @return
     */
    public static String urlEncode(String source,String encode) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source,encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "0";
        }
        return result;
    }
    public static String urlEncodeGBK(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "0";
        }
        return result;
    }
    /**
     * 发起http请求获取返回结果
     * @param req_url 请求地址
     * @return
     */
    public static String httpRequest(String req_url) {
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(req_url);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(false);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return buffer.toString();
    }

    /**
     * 发送http请求取得返回的输入流
     * @param requestUrl 请求地址
     * @return InputStream
     */
    public static InputStream httpRequestIO(String requestUrl) {
        InputStream inputStream = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();
            // 获得返回的输入流
            inputStream = httpUrlConn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }


    /**
     * 向指定URL发送GET方法的请求
     * @param url       目标网址
     * @param param     发送参数
     * @param Cookies   cookie，使用String数组，设置数组的第一个为cookies传入，便于cookie自动合并更新回传
     * @param charSet   使用的编码UTF8或者GBK
     * @param heard     自定义协议头
     * @return
     */
    public static String sendGet(String url, String param,String[] Cookies,String charSet,String heard,boolean isCdx) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);

            //设置忽略证书异常
            trustAllHttpsCertificates();
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    //logger.info("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);


            System.setProperty ("jsse.enableSNIExtension", "false");
            // 打开和URL之间的连接
            //URLConnection conn = realUrl.openConnection();
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            if (isCdx) {
                conn.setInstanceFollowRedirects(false);
                System.out.println("设定禁止重定向");
            }
            //自定义协议头处理
            if (heard!=""){
                for (String line : heard.split("\n")){
                    String[] xyt=line.split(":");
                    if (xyt.length>=2){
                        conn.setRequestProperty(xyt[0].trim(), xyt[1].trim());
                    }
                }
            }else{
                // 设置通用的请求属性

                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            conn.setRequestProperty("Cookie", Cookies[0]);
            // 建立实际的连接
            conn.connect();
            // 获取所有响应头字段

            // 定义 BufferedReader输入流来读取URL的响应
            //System.out.println("开始解码");
            if (charSet!="") {
                //System.out.println("需要转码");
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charSet));
            }else{
                //System.out.println("无需转码");
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            //System.out.println("debug");
            //in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            //System.out.println("解码完成："+result);
            //cookie处理
            Map<String, List<String>> map = conn.getHeaderFields();
            //System.out.println("显示响应Header信息...\n");
            List<String> ck = map.get("Set-Cookie");
            if (ck!=null){
                for (String values : ck) {
                    //System.out.println(values);
                    String[] s = values.split(";");
                    for (String value : s) {
                        if (value.indexOf("path=") == -1) {
                            //System.out.println("单个:" + value.trim());
                            String[] s1 = value.trim().split("=");
                            if (Cookies[0].indexOf(s1[0]) != -1) {
                                Cookies[0].replaceAll(getMiddleStr(Cookies[0], s1[0] + "=", ";"), s1[1]);
                            } else {
                                Cookies[0] += value.trim() + ";";
                            }
                        }
                    }
                }
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

    /**
     * 向指定URL发送GET方法的请求
     * @param url       目标网址
     * @param param     发送参数
     * @param Cookies   cookie，使用String数组，设置数组的第一个为cookies传入，便于cookie自动合并更新回传
     * @param charSet   使用的编码UTF8或者GBK
     * @param heard     自定义协议头
     * @return
     */
    public static String sendPost(String url, String param,String[] Cookies,String charSet,String heard,boolean isCdx) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            //HttpURLConnection conn = null;

            //设置忽略证书异常
            trustAllHttpsCertificates();
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    //logger.info("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            System.setProperty ("jsse.enableSNIExtension", "false");

            //conn = (HttpURLConnection) realUrl.openConnection();
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            if (isCdx)
                conn.setInstanceFollowRedirects(false);
            // 打开和URL之间的连接

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");    // POST方法




            //自定义协议头处理
            if (heard!=""){
                for (String line : heard.split("\n")){
                    String[] xyt=line.split(":");
                    if (xyt.length>=2){
                        conn.setRequestProperty(xyt[0].trim(), xyt[1].trim());
                    }
                }
            }else{
                //设置通用的请求属性
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }


            conn.setRequestProperty("Cookie", Cookies[0]);
            conn.connect();

            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), charSet);
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charSet));

            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            //cookie处理
            Map<String, List<String>> map = conn.getHeaderFields();
            //System.out.println("显示响应Header信息...\n");

            List<String> ck = map.get("Set-Cookie");
            if (ck!=null){
                for (String values : ck) {
                    //System.out.println(values);
                    String[] s = values.split(";");
                    for (String value : s)
                        if (value.indexOf("path=")==-1){
                            //System.out.println("单个:"+value.trim());
                            String[] s1 = value.trim().split("=");
                            if (Cookies[0].indexOf(s1[0])!=-1){
                                Cookies[0].replaceAll(getMiddleStr(Cookies[0],s1[0]+"=",";"),s1[1]);
                            }else{
                                Cookies[0]+=value.trim()+";";
                            }
                        }
                }
            }


            //Cookies[0]="123";
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    //取出中间文本
    public static String getMiddleStr(String str,String startStr,String endStr){
        String result;
        int start = str.indexOf(startStr);              // 从a开始
        if (start==-1){
            return "";
        }
        int end = str.substring(start+startStr.length()).indexOf(endStr);                   // 截止至b
        //System.out.println("START:"+start+" end:"+(start+end)+" length:"+str.length());
        if (end==-1) {
            result = str.substring(start);      // 截取结果
        }else{
            result = str.substring(start+startStr.length(), start+startStr.length() + end);      // 截取结果
        }
        return result;
    }
    public static void main(String[] args) {
        //demo:访问
        /*String url = "https://szvone.cn/util.php";
        System.out.println(url);
        String para = "name="+HttpRequestUtil.urlEncodeGBK("张炜鑫");

        url = "https://wx.qq.com/";
        HttpRequestUtil.sendGet(url,"",ck,"GBK","");
        System.out.println(ck[0]);
        ck[0]="mm_lang=zh_CN;";
        url = "https://login.wx2.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx2.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN&_="+ new Date().getTime();
        String str=HttpRequestUtil.sendGet(url,"",ck,"GBK","");
        System.out.println(url);
        System.out.println(str);

        String sr=HttpRequestUtil.sendGet(url,para,ck,"GBK","");
        System.out.println(sr);
        */
        String ck[]={""};
        String url = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage?ticket=A9Q1EARxDsmKAmbO_voElfDv@qrticket_0&uuid=ocAcruWCBg==&lang=zh_CN&scan=1508251342";
        String str=HttpRequestUtil.sendGet(url,"",ck,"GBK","",true);
        System.out.println(str);
    }

    static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }

}
