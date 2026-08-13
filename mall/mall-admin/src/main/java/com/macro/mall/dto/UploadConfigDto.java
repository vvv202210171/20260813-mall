package com.macro.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件上传配置返回结果
 */
@Data
public class UploadConfigDto {
    @Schema(description = "存储类型: local / minio / oss")
    private String type;

    @Schema(description = "上传接口URL（local/minio 使用）")
    private String uploadUrl;

    @Schema(description = "OSS 获取签名策略URL（type=oss 时有效）")
    private String ossPolicyUrl;

    @Schema(description = "OSS 回调URL（type=oss 时有效）")
    private String ossCallbackUrl;
}
