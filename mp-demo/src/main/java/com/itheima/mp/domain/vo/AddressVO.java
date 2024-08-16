package com.itheima.mp.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author 虎哥
 * @since 2023-07-01
 */
@Data
@Schema(description = "收货地址VO")
public class AddressVO{

    @Schema(name ="id")
    private Long id;

    @Schema(name ="用户ID")
    private Long userId;

    @Schema(name ="省")
    private String province;

    @Schema(name ="市")
    private String city;

    @Schema(name ="县/区")
    private String town;

    @Schema(name ="手机")
    private String mobile;

    @Schema(name ="详细地址")
    private String street;

    @Schema(name ="联系人")
    private String contact;

    @Schema(name ="是否是默认 1默认 0否")
    private Boolean isDefault;

    @Schema(name ="备注")
    private String notes;
}
