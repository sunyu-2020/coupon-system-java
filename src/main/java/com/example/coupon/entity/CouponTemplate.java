package com.example.coupon.entity;

import com.example.coupon.enums.CouponType;
import com.example.coupon.exception.CouponException;
import com.example.coupon.valueobject.Money;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 优惠券模板实体
 * 核心领域对象，包含优惠券的基本属性和限制条件
 * 包含模板ID、名称、优惠类型、优惠值、最低消费门槛、有效期、总发行量、每个用户最多领取数量等属性
 * 控制优惠券的发行总量和单个用户的领取上限
 */
@Getter
public class CouponTemplate {

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 名称
     */
    private String name;

    /**
     * 优惠类型
     */
    private CouponType couponType;

    /**
     * 优惠值
     */
    private Money couponValue;

    /**
     * 最低消费门槛
     */
    private Money minConsume;

    /**
     * 有效期开始时间
     */
    private LocalDateTime startTime;

    /**
     * 有效期结束时间
     */
    private LocalDateTime endTime;

    /**
     * 总发行量
     */
    private Integer totalQuantity;

    /**
     * 每个用户最多领取数量
     */
    private Integer userMaxQuantity;

    /**
     * 已发放数量
     */
    private Integer issuedQuantity;

    public CouponTemplate(String templateId, String name, CouponType couponType, Money couponValue, 
                         Money minConsume, LocalDateTime startTime, LocalDateTime endTime, 
                         Integer totalQuantity, Integer userMaxQuantity) {
        validateParams(name, couponType, couponValue, minConsume, startTime, endTime, 
                      totalQuantity, userMaxQuantity);
        
        this.templateId = templateId;
        this.name = name;
        this.couponType = couponType;
        this.couponValue = couponValue;
        this.minConsume = minConsume;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalQuantity = totalQuantity;
        this.userMaxQuantity = userMaxQuantity;
        this.issuedQuantity = 0;
    }

    /**
     * 验证参数
     */
    private void validateParams(String name, CouponType couponType, Money couponValue, 
                               Money minConsume, LocalDateTime startTime, LocalDateTime endTime, 
                               Integer totalQuantity, Integer userMaxQuantity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("优惠券模板名称不能为空");
        }
        if (couponType == null) {
            throw new IllegalArgumentException("优惠券类型不能为空");
        }
        if (couponValue == null) {
            throw new IllegalArgumentException("优惠券值不能为空");
        }
        if (minConsume == null) {
            throw new IllegalArgumentException("最低消费门槛不能为空");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("有效期开始时间不能为空");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("有效期结束时间不能为空");
        }
        if (totalQuantity == null || totalQuantity <= 0) {
            throw new IllegalArgumentException("总发行量必须大于0");
        }
        if (userMaxQuantity == null || userMaxQuantity <= 0) {
            throw new IllegalArgumentException("每个用户最多领取数量必须大于0");
        }
        
        // 验证模板结束时间必须晚于开始时间
        if (!endTime.isAfter(startTime)) {
            throw new CouponException("模板结束时间必须晚于开始时间");
        }
    }

    /**
     * 检查是否可以领取优惠券
     * 
     * @param userReceivedCount 用户已领取数量
     * @return 是否可以领取
     */
    public boolean canReceive(int userReceivedCount) {
        // 检查是否超过单个用户领取上限
        if (userReceivedCount >= this.userMaxQuantity) {
            return false;
        }
        
        // 检查是否超过总发行量
        if (this.issuedQuantity >= this.totalQuantity) {
            return false;
        }
        
        // 检查是否在有效期内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(this.startTime) || now.isAfter(this.endTime)) {
            return false;
        }
        
        return true;
    }

    /**
     * 增加已发行数量
     * 
     * @param quantity 增加的数量
     */
    public void increaseIssuedQuantity(int quantity) {
        if (this.issuedQuantity + quantity > this.totalQuantity) {
            throw new CouponException("已发放数量加上增加数量不能超过总发行量");
        }
        this.issuedQuantity += quantity;
    }

    /**
     * 检查是否还可以继续发放
     * 
     * @return 是否还可以继续发放
     */
    public boolean canIssueMore() {
        return this.issuedQuantity < this.totalQuantity;
    }

    // 为支持从 Model 转换到 Entity，提供必要的 setter 方法
    public void setIssuedQuantity(Integer issuedQuantity) {
        this.issuedQuantity = issuedQuantity;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CouponTemplate that = (CouponTemplate) o;
        return Objects.equals(templateId, that.templateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateId);
    }
}
