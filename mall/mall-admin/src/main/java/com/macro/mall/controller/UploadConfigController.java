package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.UploadConfigDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件上传配置接口 - 前端通过此接口获取当前使用的上传类型及相关地址
 */
@RestController
@Tag(name = "UploadConfigController", description = "文件上传配置")
@RequestMapping("/upload")
public class UploadConfigController {

    /** 存储类型：local / minio / oss */
    @Value("${upload.type:local}")
    private String uploadType;

    // local 配置
    @Value("${local.upload.url:}")
    private String localUploadUrl;

    // minio 配置
    @Value("${minio.endpoint:}")
    private String minioEndpoint;
    @Value("${minio.bucketName:}")
    private String minioBucketName;

    // oss 配置（前端直传）
    @Value("${aliyun.oss.callback:}")
    private String ossCallbackUrl;

    @Operation(summary = "获取文件上传配置（前端根据 type 决定上传方式）")
    @GetMapping("/config")
    public CommonResult<UploadConfigDto> getUploadConfig() {
        UploadConfigDto dto = new UploadConfigDto();
        dto.setType(uploadType);
        switch (uploadType) {
            case "minio":
                dto.setUploadUrl("/minio/upload");
                break;
            case "oss":
                dto.setOssPolicyUrl("/aliyun/oss/policy");
                dto.setOssCallbackUrl(ossCallbackUrl);
                break;
            case "local":
            default:
                dto.setUploadUrl("/local/upload");
        }
        return CommonResult.success(dto);
    }
}
