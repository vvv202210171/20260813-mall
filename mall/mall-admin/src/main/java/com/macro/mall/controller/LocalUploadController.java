package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.MinioUploadDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 本地文件存储管理Controller
 */
@Controller
@Tag(name = "LocalUploadController", description = "本地文件存储管理")
@RequestMapping("/local")
public class LocalUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalUploadController.class);

    /** 本地存储根目录，例如 /data/upload 或 D:/upload */
    @Value("${local.upload.path}")
    private String uploadPath;

    /** 文件访问基础URL，例如 http://localhost:8080/local/files */
    @Value("${local.upload.url}")
    private String uploadUrl;

    @Operation(summary = "本地文件上传")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public CommonResult<MinioUploadDto> upload(@RequestPart("file") MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            // 按日期分目录，防止单目录文件过多
            String datePath = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String newFilename = UUID.randomUUID().toString().replace("-", "") + suffix;
            String relativePath = datePath + "/" + newFilename;

            // 创建目录
            File dir = new File(uploadPath + "/" + datePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存文件
            File dest = new File(uploadPath + "/" + relativePath);
            file.transferTo(dest);
            LOGGER.info("本地文件上传成功: {}", dest.getAbsolutePath());

            MinioUploadDto dto = new MinioUploadDto();
            dto.setName(originalFilename);
            dto.setUrl(uploadUrl + "/" + relativePath);
            return CommonResult.success(dto);
        } catch (IOException e) {
            LOGGER.error("本地文件上传失败: {}", e.getMessage(), e);
        }
        return CommonResult.failed("文件上传失败");
    }

    @Operation(summary = "本地文件删除")
    @PostMapping(value = "/delete")
    @ResponseBody
    public CommonResult<Void> delete(@RequestParam("objectName") String objectName) {
        // objectName 为相对路径，如 20240101/xxx.jpg
        File file = new File(uploadPath + "/" + objectName);
        if (file.exists() && file.delete()) {
            LOGGER.info("本地文件删除成功: {}", file.getAbsolutePath());
            return CommonResult.success(null);
        }
        return CommonResult.failed("文件删除失败或文件不存在");
    }
}
