package com.itheima.mp.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户表单实体")
public class UserFormDTO {

    @Schema(name = "id")
    private Long id;

    @Schema(name = "用户名")
    private String username;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "注册手机号")
    private String phone;

    @Schema(name = "详细信息，JSON风格")
    private String info;

    @Schema(name = "账户余额")
    private Integer balance;
}
