package com.example.coupon.dto;

import com.example.coupon.enums.CouponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

/**
 * 优惠券模板创建请求DTO
 * 使用Lombok注解减少样板代码
 * 金额字段使用Long类型（单位：分），在Service层转换为Money值对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponTemplateCreateRequest {
    /**
     * 模板ID
     */
    @NotBlank(message = "模板ID不能为空")
    private String templateId;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空")
    private String name;
    
    /**
     * 优惠券类型
     */
    @NotNull(message = "优惠券类型不能为空")
    private CouponType couponType;
    
    /**
     * 优惠券金额（单位：分）
     */
    @NotNull(message = "优惠券金额不能为空")
    @Min(value = 0, message = "优惠券金额不能小于0")
    private Long couponValue;
    
    /**
     * 最低消费金额（单位：分）
     */
    @NotNull(message = "最低消费金额不能为空")
    @Min(value = 0, message = "最低消费金额不能小于0")
    private Long minConsume;
    
    /**
     * 有效期开始时间
     */
    @NotNull(message = "有效期开始时间不能为空")
    private LocalDateTime startTime;
    
    /**
     * 有效期结束时间
     */
    @NotNull(message = "有效期结束时间不能为空")
    private LocalDateTime endTime;
    
    /**
     * 总发行量
     */
    @NotNull(message = "总发行量不能为空")
    @Min(value = 1, message = "总发行量必须大于0")
    private Integer totalQuantity;
    
    /**
     * 每个用户最多领取数量
     */
    @NotNull(message = "每个用户最多领取数量不能为空")
    @Min(value = 1, message = "每个用户最多领取数量必须大于0")
    private Integer userMaxQuantity;
}
