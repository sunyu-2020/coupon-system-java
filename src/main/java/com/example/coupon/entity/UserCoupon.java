package com.example.coupon.entity;

import com.example.coupon.enums.CouponStatus;
import com.example.coupon.exception.CouponException;
import com.example.coupon.valueobject.Money;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 用户优惠券实体
 * 代表用户实际持有的优惠券实例
 */
@Getter
public class UserCoupon {
    /**
     * 优惠券ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 优惠券金额
     */
    private Money couponAmount;

    /**
     * 最低消费金额
     */
    private Money minConsumeAmount;

    /**
     * 有效期开始时间
     */
    private LocalDateTime validStartTime;

    /**
     * 有效期结束时间
     */
    private LocalDateTime validEndTime;

    /**
     * 优惠券状态
     */
    private CouponStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public UserCoupon(Long userId, String templateId, Money couponAmount, 
                      Money minConsumeAmount, LocalDateTime validStartTime, 
                      LocalDateTime validEndTime) {
        this.userId = userId;
        this.templateId = templateId;
        this.couponAmount = couponAmount;
        this.minConsumeAmount = minConsumeAmount;
        this.validStartTime = validStartTime;
        this.validEndTime = validEndTime;
        this.status = CouponStatus.NEW; // 默认为NEW状态
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        
        validate();
    }

    /**
     * 验证用户优惠券参数
     */
    private void validate() {
        if (userId == null) {
            throw new CouponException("用户ID不能为空");
        }
        if (templateId == null) {
            throw new CouponException("模板ID不能为空");
        }
        if (couponAmount == null) {
            throw new CouponException("优惠券金额不能为空");
        }
        if (minConsumeAmount == null) {
            throw new CouponException("最低消费金额不能为空");
        }
        if (validStartTime == null) {
            throw new CouponException("有效期开始时间不能为空");
        }
        if (validEndTime == null) {
            throw new CouponException("有效期结束时间不能为空");
        }
        if (validStartTime.isAfter(validEndTime)) {
            throw new CouponException("有效期开始时间不能晚于结束时间");
        }
    }

    /**
     * 核销优惠券
     * @param orderAmount 订单金额
     * @return 核销后的优惠券
     */
    public UserCoupon use(Money orderAmount) {
        // 验证状态是否为NEW
        if (!CouponStatus.NEW.equals(this.status)) {
            throw new CouponException("优惠券状态不为NEW，无法核销");
        }

        // 验证是否在有效期内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(this.validStartTime) || now.isAfter(this.validEndTime)) {
            throw new CouponException("优惠券不在有效期内");
        }

        // 验证订单金额是否满足最低消费要求
        if (orderAmount == null || !orderAmount.greaterThanOrEqual(this.minConsumeAmount)) {
            throw new CouponException("订单金额不满足最低消费要求");
        }

        // 更新状态为USED
        this.status = CouponStatus.USED;
        this.updateTime = LocalDateTime.now();

        return this;
    }

    /**
     * 检查优惠券是否可用
     * @return 是否可用
     */
    public boolean isAvailable() {
        if (!CouponStatus.NEW.equals(this.status)) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(this.validStartTime) && !now.isAfter(this.validEndTime);
    }

    /**
     * 检查是否已过期
     * @return 是否已过期
     */
    public boolean isExpired() {
        if (CouponStatus.EXPIRED.equals(this.status)) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.validEndTime);
    }

    // 为支持从 Model 转换到 Entity，提供必要的 setter 方法
    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCoupon that = (UserCoupon) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
