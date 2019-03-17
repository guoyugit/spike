package com.gy;

import org.apache.commons.lang3.CharSet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * ClassName: Test
 * Description:
 * date: 2019/3/17 16:47
 *
 * @author 郭宇
 * @since JDK 1.8
 */
public class Test {

    public static void main(String[] args) throws IOException {
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        defaultResourceLoader.addProtocolResolver((location, loader) -> {
            return new ClassPathResource(location);
        });
        Resource resource = defaultResourceLoader.getResource("application.yaml");
        InputStream inputStream = resource.getInputStream();
        System.out.println(StreamUtils.copyToString(inputStream, Charset.forName("utf-8")));

    }
}