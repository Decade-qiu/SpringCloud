package com.itheima.mp.domain.vo;

import com.itheima.mp.domain.enums.UserStatus;
import com.itheima.mp.domain.po.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户VO实体")
public class UserVO {

    @Schema(name ="用户id")
    private Long id;

    @Schema(name ="用户名")
    private String username;

    @Schema(name ="详细信息")
    private String info;

    @Schema(name ="使用状态（1正常 2冻结）")
    private UserStatus status;

    @Schema(name ="账户余额")
    private Integer balance;
}
