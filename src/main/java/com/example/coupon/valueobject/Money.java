package com.example.coupon.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额值对象
 */
public class Money {
    /**
     * 金额，单位为分
     */
    private final Long amount;

    public Money(Long amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("金额不能为null且必须大于等于0");
        }
        this.amount = amount;
    }

    public Long getAmount() {
        return amount;
    }

    /**
     * 从分创建Money对象
     *
     * @param fen 金额，单位分
     * @return Money对象
     */
    public static Money ofFen(Long fen) {
        return new Money(fen);
    }

    /**
     * 从元创建Money对象
     *
     * @param yuan 金额，单位元
     * @return Money对象
     */
    public static Money ofYuan(BigDecimal yuan) {
        if (yuan == null) {
            throw new IllegalArgumentException("金额不能为null");
        }
        BigDecimal fen = yuan.multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN);
        return new Money(fen.longValue());
    }

    /**
     * 判断当前金额是否大于等于指定金额
     *
     * @param other 指定金额
     * @return 是否大于等于
     */
    public boolean greaterThanOrEqual(Money other) {
        return this.amount >= other.amount;
    }

    /**
     * 判断当前金额是否大于指定金额
     *
     * @param other 指定金额
     * @return 是否大于
     */
    public boolean greaterThan(Money other) {
        return this.amount > other.amount;
    }

    /**
     * 加法运算
     *
     * @param other 另一个金额
     * @return 相加后的金额
     */
    public Money add(Money other) {
        return new Money(this.amount + other.amount);
    }

    /**
     * 减法运算
     *
     * @param other 另一个金额
     * @return 相减后的金额
     */
    public Money subtract(Money other) {
        long result = this.amount - other.amount;
        if (result < 0) {
            throw new IllegalArgumentException("减法运算结果不能小于0");
        }
        return new Money(result);
    }

    /**
     * 转换为元
     *
     * @return 金额，单位元
     */
    public BigDecimal toYuan() {
        return new BigDecimal(amount).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
    }
}
