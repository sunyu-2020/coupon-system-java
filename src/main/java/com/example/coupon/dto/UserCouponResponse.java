package com.example.coupon.dto;

import com.example.coupon.enums.CouponStatus;
import com.example.coupon.valueobject.Money;

import java.time.LocalDateTime;

/**
 * 用户优惠券查询响应 DTO
 * 用于避免直接暴露领域实体给外部
 */
public class UserCouponResponse {
    private Long id;
    private String couponCode;  // 优惠券唯一编码
    private Long userId;
    private String templateId;  // 模板ID
    private Money couponAmount; // 优惠券金额
    private Money minConsumeAmount; // 最低消费金额
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;
    private CouponStatus status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public UserCouponResponse() {}

    public UserCouponResponse(Long id, String couponCode, Long userId, String templateId,
                          Money couponAmount, Money minConsumeAmount, LocalDateTime validStartTime,
                          LocalDateTime validEndTime, CouponStatus status, LocalDateTime createTime,
                          LocalDateTime updateTime) {
        this.id = id;
        this.couponCode = couponCode;
        this.userId = userId;
        this.templateId = templateId;
        this.couponAmount = couponAmount;
        this.minConsumeAmount = minConsumeAmount;
        this.validStartTime = validStartTime;
        this.validEndTime = validEndTime;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Money getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Money couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Money getMinConsumeAmount() {
        return minConsumeAmount;
    }

    public void setMinConsumeAmount(Money minConsumeAmount) {
        this.minConsumeAmount = minConsumeAmount;
    }

    public LocalDateTime getValidStartTime() {
        return validStartTime;
    }

    public void setValidStartTime(LocalDateTime validStartTime) {
        this.validStartTime = validStartTime;
    }

    public LocalDateTime getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(LocalDateTime validEndTime) {
        this.validEndTime = validEndTime;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
