package com.macro.mall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 本地文件静态资源映射配置
 * 将本地上传目录映射到 /local/files/** URL，使文件可通过HTTP访问
 */
@Configuration
public class LocalUploadConfig implements WebMvcConfigurer {

    @Value("${local.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保路径以 file:// 开头且以 / 结尾
        String location = uploadPath.startsWith("file:") ? uploadPath : "file:" + uploadPath;
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler("/local/files/**")
                .addResourceLocations(location);
    }
}
