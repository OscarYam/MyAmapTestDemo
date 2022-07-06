package com.example.myamaptestdemo.data.utility;

import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.myamaptestdemo.data.model.ResponResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class utility {
    public utility() {}

    /**
     * 随机数生成器
     * 生成 n 个 [0-9] 之间的随机整数
     * */
    public static String RamNum(int n) {
        StringBuilder sr = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            int num = r.nextInt(9); // 生成[0,9]区间的随机整数
            sr.append(num);
        }
        return sr.toString();
    }


    /**
     * BASE64 编码
     * */
    public static String Encode_BASE64(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * BASE64 解码
     * */
    public static byte[] Decode_BASE64(String data) throws IllegalArgumentException {
        return Base64.decode(data, Base64.DEFAULT);
    }


    /**
     * MD5/SHA-1/SHA-256 编码
     * @param mode "MD5" / "SHA-1" / "SHA-256"
     * */
    public static String Encode_MD5_SHA(String data, String mode) throws Exception {
        MessageDigest md = MessageDigest.getInstance(mode);
        md.update(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : md.digest()) {
            if ((b & 0xff) < 0x10) {
                // 小于0x10的在前方补 "0"
                sb.append("0").append(Integer.toHexString(b & 0xff));
            } else {
                sb.append(Integer.toHexString(b & 0xff));
            }
        }
        return sb.toString();
    }

    public static final String algorithm = "AES";
    // AES/CBC/NOPaddin
    // AES 默认模式
    // 使用CBC模式, 在初始化Cipher对象时, 需要增加参数, 初始化向量IV : IvParameterSpec iv = new
    // IvParameterSpec(key.getBytes());
    // NOPadding: 使用NOPadding模式时, 原文长度必须是8byte的整数倍
    public static final String transformation = "AES/CBC/NOPadding";
    public static final String key = "1234567812345671"; // must be 16 bytes long

    /**
     * AES 加密
     * @param data 需要加密的参数 (注意必须是 16 bytes 或 16 x n bytes)
     * */
    public static String Encrypt_AES(String data) throws Exception {
        // 获取Cipher
        Cipher cipher = Cipher.getInstance(transformation);
        // 生成密钥
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
        // 指定模式(加密)和密钥
        // 创建初始化向量
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        // 加密
        byte[] bytes = cipher.doFinal(data.getBytes());
        // BASE64 编码
        return Encode_BASE64(bytes);
    }

    /**
     * AES 解密
     * @param data 需要解密的参数
     * */
    public static String Decrypt_AES(String data) throws Exception {
        // 获取Cipher
        Cipher cipher = Cipher.getInstance(transformation);
        // 生成密钥
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
        // 指定模式(解密)和密钥
        // 创建初始化向量
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        // 解密
        byte[] bytes = cipher.doFinal(Decode_BASE64(data));
        return new String(bytes);
    }

    /**
     * http post request.
     * */
//    public static String httpPostReq(String strData) throws Exception {
//        String result = null;
//        byte[] requestBody = strData.getBytes(StandardCharsets.UTF_8);
//
//        URL url = new URL("http://192.168.2.125:8081/home");
////        URL url = new URL("http://10.141.51.198:80/home"); // 路由器设置虚拟服务器用
//
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        httpURLConnection.setConnectTimeout(3000); //设置连接超时时间
//        httpURLConnection.setReadTimeout(8000); // 设置读取超时时间 10000
//        httpURLConnection.setDoInput(true); //打开输入流，以便从服务器获取数据
//        httpURLConnection.setDoOutput(true); //打开输出流，以便向服务器提交数据
//        httpURLConnection.setRequestMethod("POST"); //设置以Post方式提交数据
//        httpURLConnection.setUseCaches(false); //使用Post方式不能使用缓存
//        httpURLConnection.setRequestProperty("Content-Type", "application/json"); //设置请求体的类型是文本类型
//        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(requestBody.length)); //设置请求体的长度
//        OutputStream outputStream = httpURLConnection.getOutputStream();
//        outputStream.write(requestBody);
//        outputStream.flush();
//        int response = httpURLConnection.getResponseCode(); //获得服务器的响应码
//        if (response == HttpURLConnection.HTTP_OK) {
//            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            StringBuilder strBuilder = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                strBuilder.append(line);
//            }
//            result = strBuilder.toString();
//            inputStreamReader.close();
////            httpURLConnection.disconnect();
//        }
//
//        outputStream.close();
//        httpURLConnection.disconnect();
//
//        return result;
//    }
    public static String httpPostReq(String strData) {
        String result = null;
        byte[] requestBody = strData.getBytes(StandardCharsets.UTF_8);

        URL url = null;
        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;
        InputStreamReader inputStreamReader = null;

        try {
            url = new URL("http://192.168.2.125:8081/home");
//            URL url = new URL("http://10.141.51.198:80/home"); // 路由器设置虚拟服务器用

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000); //设置连接超时时间
//            httpURLConnection.setReadTimeout(8000); // 设置读取超时时间 10000
            httpURLConnection.setReadTimeout(3000); // 设置读取超时时间 10000
            httpURLConnection.setDoInput(true); //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true); //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST"); //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false); //使用Post方式不能使用缓存
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(requestBody.length)); //设置请求体的长度

            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(requestBody);
            outputStream.flush();
            int response = httpURLConnection.getResponseCode(); //获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {
                inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder strBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    strBuilder.append(line);
                }
                result = strBuilder.toString();
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return result;
    }

    public static String encodeData(Object data) {
        StringBuilder sb = new StringBuilder(JSONObject.toJSONString(data));
        int l = sb.toString().getBytes(StandardCharsets.UTF_8).length % 16;
        if (l > 0) {
            for(int i=0, j=16-l; i<j; i++) {
                sb.append(RamNum(1));
            }
        }

        if (sb.toString().getBytes(StandardCharsets.UTF_8).length < 640) {
            sb.append(utility.RamNum(640));
        }

        return sb.toString();
    }

    public static Object decodeData(String data) throws Exception {
        String ret_d = Decrypt_AES(data);

        int start_index = ret_d.indexOf("{");
        int end_index = ret_d.lastIndexOf("}") + 1;
        ret_d = ret_d.substring(start_index, end_index);

        ResponResult result = new ResponResult();

        JSONObject jsObj = JSONObject.parseObject(ret_d);
        Integer code = jsObj.getInteger("code");
        if (code != null) {
            result.setCode(jsObj.getInteger("code"));
        }
        result.setMessage(jsObj.getString("message"));
        result.setStatus(jsObj.getString("status"));

        try {
            result.setData(jsObj.getJSONArray("data"));
        } catch (Exception exception) {
//                    exception.printStackTrace();
            try {
                result.setData(jsObj.getJSONObject("data"));
            } catch (Exception exception1) {
                result.setData(jsObj.getString("data"));
            }
        }

        return result;
    }
}
