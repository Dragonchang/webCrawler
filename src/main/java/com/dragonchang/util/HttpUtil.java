package com.dragonchang.util;

import com.aliyun.oss.common.utils.IOUtils;
import feign.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * springframework http Util
 * </p>
 *
 * @author gaox
 * @since 2020/3/26
 */
public class HttpUtil {


    /**
     * @param urlString url地址
     * @param fileName  文件名
     * @param suffix    文件后缀
     * @return
     */
    public static ResponseEntity<byte[]> generateHttpEntityByUrl(String urlString, String fileName, String suffix) {
        byte[] bytes = null;
        try {
            URL url = new URL(urlString);
            InputStream inputStream = url.openStream();
            bytes = IOUtils.readStreamAsByteArray(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generateHttpEntity(bytes, fileName, suffix);
    }


    /**
     * Generate HttpEntity for file download
     *
     * @param bytes    数据流
     * @param fileName 文件名
     * @param suffix   文件后缀
     * @return
     */
    public static ResponseEntity<byte[]> generateHttpEntity(byte[] bytes, String fileName, String suffix) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setCacheControl("max-age=604800");
        try {
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName + suffix, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
    }

    /**
     * Generate ResponseEntity For FeignClient request download Api.
     *  //TODO... Feign Client 请求大文件流存在请求时间过长且文件不完整的情况，
     *  因此先废弃
     *
     * @param response
     * @return
     */
    @Deprecated
    public static ResponseEntity<byte[]> generateResponseEntity4FeignClient(Response response) {
        HttpHeaders headers = new HttpHeaders();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        for (Object key : response.headers().keySet()) {
            List<String> kList = (List<String>) response.headers().get(key);
            for (String val : kList) {
                headers.add(key.toString(), val);
            }
        }
        byte[] bytes = null;
        try {
            InputStream inputStream = response.body().asInputStream();
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            bout.write(b);
            bout.flush();
            bytes = bout.toByteArray();
            inputStream.close();
            bout.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(bytes, headers, HttpStatus.CREATED);
    }


}
