package com.itheima.mp.domain.query;

import com.itheima.mp.domain.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "用户查询条件实体")
public class UserQuery extends PageQuery {
    @Schema(name ="用户名关键字")
    private String name;
    @Schema(name ="用户状态：1-正常，2-冻结")
    private UserStatus status;
    @Schema(name ="余额最小值")
    private Integer minBalance;
    @Schema(name ="余额最大值")
    private Integer maxBalance;
    @Override
    public String toString() {
        return "UserQuery{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", minBalance=" + minBalance +
                ", maxBalance=" + maxBalance +
                "} " + super.toString();
    }
}
