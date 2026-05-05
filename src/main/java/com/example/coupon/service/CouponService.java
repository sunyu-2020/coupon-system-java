package com.example.coupon.service;

import com.example.coupon.converter.UserCouponConverter;
import com.example.coupon.entity.CouponTemplate;
import com.example.coupon.entity.UserCoupon;
import com.example.coupon.enums.CouponStatus;
import com.example.coupon.exception.CouponException;
import com.example.coupon.model.CouponTemplateModel;
import com.example.coupon.model.UserCouponModel;
import com.example.coupon.repository.CouponTemplateRepository;
import com.example.coupon.repository.UserCouponRepository;
import com.example.coupon.valueobject.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** 
 * 优惠券应用服务
 * 实现业务用例编排，协调领域对象完成业务逻辑
 */
@Service
public class CouponService {
    
    private final CouponTemplateRepository couponTemplateRepository;
    private final UserCouponRepository userCouponRepository;
    
    public CouponService(CouponTemplateRepository couponTemplateRepository,
                         UserCouponRepository userCouponRepository) {
        this.couponTemplateRepository = couponTemplateRepository;
        this.userCouponRepository = userCouponRepository;
    }
    
    /**
     * 创建优惠券模板
     */
    @Transactional
    public String createCouponTemplate(String templateId, String name,
                                      com.example.coupon.enums.CouponType couponType,
                                      Money couponValue, Money minConsume,
                                      LocalDateTime startTime, LocalDateTime endTime,
                                      Integer totalQuantity, Integer userMaxQuantity) {
        // 创建领域对象并验证参数
        CouponTemplate template = new CouponTemplate(templateId, name, couponType,
                couponValue, minConsume, startTime,
                endTime, totalQuantity, userMaxQuantity);
        
        // 保存到数据库
        CouponTemplateModel model = new CouponTemplateModel(templateId, name, couponType,
                couponValue.getAmount(),
                minConsume.getAmount(),
                startTime, endTime,
                totalQuantity, userMaxQuantity);
        couponTemplateRepository.save(model);
        
        return templateId;
    }
    
    /**
     * 用户领取优惠券
     */
    @Transactional
    public String receiveCoupon(Long userId, String templateId) {
        // 从数据库获取模板信息
        CouponTemplateModel templateModel = couponTemplateRepository.findByTemplateId(templateId);
        if (templateModel == null) {
            throw new CouponException("优惠券模板不存在: " + templateId);
        }
        
        // 创建领域对象
        CouponTemplate template = new CouponTemplate(
                templateModel.getTemplateId(),
                templateModel.getName(),
                templateModel.getCouponType(),
                new Money(templateModel.getCouponValue()),
                new Money(templateModel.getMinConsume()),
                templateModel.getStartTime(),
                templateModel.getEndTime(),
                templateModel.getTotalQuantity(),
                templateModel.getUserMaxQuantity()
        );
        
        // 检查用户已领取的数量
        List<UserCouponModel> userCoupons = userCouponRepository.findByUserId(userId);
        long userReceivedCount = userCoupons.stream()
                .filter(coupon -> templateId.equals(coupon.getTemplateId()))
                .count();
        
        // 检查是否可以领取
        if (!template.canReceive((int) userReceivedCount)) {
            throw new CouponException("不符合领取条件，可能原因：已达到个人领取上限、模板已发完或不在有效期内");
        }
        
        // 生成优惠券编码
        String couponCode = "COUPON_" + System.currentTimeMillis() + "_" + userId;
        
        // 保存到数据库
        UserCouponModel userCouponModel = new UserCouponModel(
                couponCode,
                userId,
                templateId,
                templateModel.getCouponValue(),
                templateModel.getMinConsume(),
                templateModel.getStartTime(),
                templateModel.getEndTime()
        );
        userCouponRepository.save(userCouponModel);
        
        // 更新模板的已发行数量
        couponTemplateRepository.incrementIssuedQuantity(templateId, 1);
        
        return couponCode;
    }
    
    /**
     * 查询用户优惠券列表
     */
    @Transactional(readOnly = true)
    public List<UserCoupon> queryUserCoupons(Long userId) {
        List<UserCouponModel> models = userCouponRepository.findByUserId(userId);
        return models.stream().map(model -> UserCouponConverter.INSTANCE.toEntity(model))
                .collect(Collectors.toList());
    }
    
    /**
     * 核销优惠券
     */
    @Transactional
    public boolean useCoupon(String couponCode, Long userId, Money orderAmount) {
        // 获取用户优惠券
        UserCouponModel model = userCouponRepository.findByCouponCode(couponCode);
        if (model == null) {
            throw new CouponException("优惠券不存在: " + couponCode);
        }
        
        // 检查优惠券是否属于该用户
        if (!userId.equals(model.getUserId())) {
            throw new CouponException("优惠券不属于当前用户");
        }
        
        // 使用转换器创建领域对象
        UserCoupon userCoupon = UserCouponConverter.INSTANCE.toEntity(model);
        
        // 使用领域对象进行核销验证和操作
        userCoupon.use(orderAmount);
        
        // 更新数据库中的状态
        userCouponRepository.updateStatus(couponCode, CouponStatus.USED.getCode());
        
        return true;
    }
}
