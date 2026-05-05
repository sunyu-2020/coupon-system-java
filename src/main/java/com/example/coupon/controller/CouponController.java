package com.example.coupon.controller;

import com.example.coupon.dto.CouponTemplateCreateRequest;
import com.example.coupon.dto.UseCouponRequest;
import com.example.coupon.dto.UserCouponResponse;
import com.example.coupon.entity.UserCoupon;
import com.example.coupon.exception.CouponException;
import com.example.coupon.service.CouponService;
import com.example.coupon.valueobject.Money;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 优惠券REST API控制器
 */
@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    /**
     * 创建优惠券模板
     * POST /api/coupon/templates
     */
    @PostMapping("/templates")
    public ResponseEntity<Map<String, Object>> createTemplate(@Valid @RequestBody CouponTemplateCreateRequest request) {
        String templateId = couponService.createCouponTemplate(
                request.getTemplateId(),
                request.getName(),
                request.getCouponType(),
                new Money(request.getCouponValue()),
                new Money(request.getMinConsume()),
                request.getStartTime(),
                request.getEndTime(),
                request.getTotalQuantity(),
                request.getUserMaxQuantity()
        );

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("templateId", templateId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * 领取优惠券
     * POST /api/coupon/users/{userId}/receive?templateId=xxx
     */
    @PostMapping("/users/{userId}/receive")
    public ResponseEntity<Map<String, Object>> receiveCoupon(
            @PathVariable Long userId,
            @RequestParam String templateId) {
        String couponCode = couponService.receiveCoupon(userId, templateId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("couponCode", couponCode);
        return ResponseEntity.ok(result);
    }

    /**
     * 查询用户优惠券列表
     * GET /api/coupon/users/{userId}/coupons
     */
    @GetMapping("/users/{userId}/coupons")
    public ResponseEntity<List<UserCouponResponse>> queryUserCoupons(@PathVariable Long userId) {
        List<UserCoupon> userCoupons = couponService.queryUserCoupons(userId);
        List<UserCouponResponse> responses = userCoupons.stream()
                .map(entity -> new UserCouponResponse(
                        entity.getId(),
                        null, // couponCode 通常不会直接从实体获取
                        entity.getUserId(),
                        entity.getTemplateId(),
                        entity.getCouponAmount(),
                        entity.getMinConsumeAmount(),
                        entity.getValidStartTime(),
                        entity.getValidEndTime(),
                        entity.getStatus(),
                        entity.getCreateTime(),
                        entity.getUpdateTime()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * 核销优惠券
     * POST /api/coupon/users/{userId}/use
     */
    @PostMapping("/users/{userId}/use")
    public ResponseEntity<Map<String, Object>> useCoupon(
            @PathVariable Long userId,
            @Valid @RequestBody UseCouponRequest request) {
        Money orderAmount = Money.ofYuan(new BigDecimal(request.getOrderAmount()));
        boolean success = couponService.useCoupon(request.getCouponCode(), userId, orderAmount);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", success);
        return ResponseEntity.ok(result);
    }

    /**
     * 全局异常处理
     */
    @ExceptionHandler(CouponException.class)
    public ResponseEntity<Map<String, Object>> handleCouponException(CouponException e) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
