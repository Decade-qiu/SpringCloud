package com.itheima.mp.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author decade
 * @since 2024-08-14
 */
@Data
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("province")
    private String province;

    @TableField("city")
    private String city;

    @TableField("town")
    private String town;

    @TableField("mobile")
    private String mobile;

    @TableField("street")
    private String street;

    @TableField("contact")
    private String contact;

    @TableField("is_default")
    private Boolean isDefault;

    @TableField("notes")
    private String notes;

    @TableField("deleted")
    private Boolean deleted;


}
