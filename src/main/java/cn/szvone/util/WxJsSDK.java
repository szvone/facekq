package cn.szvone.util;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;




public class WxJsSDK {
    public static String token = null;
    public static String time = null;
    public static String jsapi_ticket = null;

    public static String appId = "wx69f746dd9e0b7d40";
    public static String appSecret = "da441a0ffdc804c56f9838d784865906";

    public static String getNowtoken(){
        if (token == null) {
            token = getAccess_token(appId, appSecret);
            jsapi_ticket = getJsApiTicket(token);
            time = getTime();
        } else {
            if (!time.substring(0, 13).equals(getTime().substring(0, 13))) { //每小时刷新一次
                token = null;
                token = getAccess_token(appId, appSecret);
                jsapi_ticket = getJsApiTicket(token);
                time = getTime();
            }
        }
        return token;
    }

    public static String getOpenid(String code) {
        String url ="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+"&secret="+appSecret+"&code="+code+"&grant_type=authorization_code";
        // 发起GET请求获取凭证
        String ck[] = {""};
        String str = HttpRequestUtil.sendGet(url, "", ck,"UTF8","",false);
        JSONObject jsonObject = JSONObject.fromObject(str);
        String opendi = null;
        if (null != jsonObject) {
            try {
                opendi = jsonObject.getString("openid");
                String errcode = jsonObject.getString("errcode");
                if (errcode==null)
                    return null;
            } catch (Exception e) {
                // 获取token失败
                System.out.println("获取openid失败 ");
            }
        }
        return opendi;
    }

    /**
     * @param url       当前网页的URL，不包含#及其后面部分
     * @return
     */
    public static String getParam(String url) {
        if (token == null) {
            token = getAccess_token(appId, appSecret);
            jsapi_ticket = getJsApiTicket(token);
            time = getTime();
        } else {
            if (!time.substring(0, 13).equals(getTime().substring(0, 13))) { //每小时刷新一次
                token = null;
                token = getAccess_token(appId, appSecret);
                jsapi_ticket = getJsApiTicket(token);
                time = getTime();
            }
        }

        Map<String, String> params = sign(jsapi_ticket, url);
        params.put("appId", appId);

        JSONObject jsonObject = JSONObject.fromObject(params);
        String jsonStr = jsonObject.toString();
        System.out.println(jsonStr);
        return jsonStr;
    }


    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String str;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        str = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    //获取当前系统时间 用来判断access_token是否过期
    public static String getTime() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dt);
    }


    /**
     * 调用微信JS接口的临时票据
     *
     * @param access_token 接口访问凭证
     * @return
     */
    public static String getJsApiTicket(String access_token) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
        String requestUrl = url.replace("ACCESS_TOKEN", access_token);
        System.out.println(requestUrl);
        // 发起GET请求获取凭证
        String ck[] = {""};
        String str = HttpRequestUtil.sendGet(requestUrl, "", ck,"UTF8","",false);
        JSONObject jsonObject = JSONObject.fromObject(str);
        String ticket = null;
        if (null != jsonObject) {
            try {
                ticket = jsonObject.getString("ticket");
            } catch (JSONException e) {
                // 获取token失败
                System.out.println("获取token失败 "+jsonObject.getInt("errcode")+"-"+jsonObject.getString("errmsg"));
            }
        }
        return ticket;
    }

    /**
     * 获取接口访问凭证
     *
     * @param appid 凭证
     * @param appsecret 密钥
     * @return
     */
    public static String getAccess_token(String appid, String appsecret) {
        //凭证获取(GET)
        String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        System.out.println(requestUrl);
        // 发起GET请求获取凭证
        String ck[] = {""};
        String str = HttpRequestUtil.sendGet(requestUrl, "", ck,"UTF8","",false);
        System.out.println(str);
        JSONObject jsonObject = JSONObject.fromObject(str);
        String access_token = null;
        if (null != jsonObject) {
            try {
                access_token = jsonObject.getString("access_token");
            } catch (JSONException e) {
                // 获取token失败
                System.out.println("获取token失败 "+jsonObject.getInt("errcode")+"-"+jsonObject.getString("errmsg"));
            }
        }
        return access_token;
    }



    /**
     * 根据内容类型判断文件扩展名
     *
     * @param contentType 内容类型
     * @return
     */
    public static String getFileexpandedName(String contentType) {
        String fileEndWitsh = "";
        if ("image/jpeg".equals(contentType))
            fileEndWitsh = ".jpg";
        else if ("audio/mpeg".equals(contentType))
            fileEndWitsh = ".mp3";
        else if ("audio/amr".equals(contentType))
            fileEndWitsh = ".amr";
        else if ("video/mp4".equals(contentType))
            fileEndWitsh = ".mp4";
        else if ("video/mpeg4".equals(contentType))
            fileEndWitsh = ".mp4";
        return fileEndWitsh;
    }
    /**
     * 获取媒体文件
     * @param mediaId 媒体文件id
     * @param savePath 文件在本地服务器上的存储路径
     * */
    public static String downloadMedia(String mediaId, String savePath) {

        String accessToken = getNowtoken();

        String filePath = null;
        // 拼接请求地址
        String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");

            if (!savePath.endsWith("/")) {
                savePath += "/";
            }
            // 根据内容类型获取扩展名
            String fileExt = getFileexpandedName(conn.getHeaderField("Content-Type"));
            // 将mediaId作为文件名
            String returnPath = new Date().getTime()+fileExt;
            filePath = savePath + returnPath ;
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            File file = new File(filePath);
            file.createNewFile();
            file.setReadable(true, false);
//            file.setExecutable(true, false);
//            file.setWritable(true, false);

            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[8096];
            int size = 0;
            while ((size = bis.read(buf)) != -1)
                fos.write(buf, 0, size);
            fos.close();
            bis.close();

            conn.disconnect();
            String info = String.format("下载媒体文件成功，filePath=" + filePath);
            System.out.println(info);
            return returnPath;
        } catch (Exception e) {
            filePath = null;
            String error = String.format("下载媒体文件失败：%s", e);
            System.out.println(error);
        }
        return filePath;
    }
}

