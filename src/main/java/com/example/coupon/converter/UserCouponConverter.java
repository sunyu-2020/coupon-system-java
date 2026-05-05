package com.example.coupon.converter;

import com.example.coupon.entity.UserCoupon;
import com.example.coupon.model.UserCouponModel;
import com.example.coupon.valueobject.Money;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/** 
 * UserCoupon 实体和模型之间的转换器
 * 使用 MapStruct 自动生成转换代码
 */
@Mapper
public interface UserCouponConverter {
    
    UserCouponConverter INSTANCE = Mappers.getMapper(UserCouponConverter.class);
    
    // Model 转 Entity
    @Mapping(source = "couponValue", target = "couponAmount", qualifiedByName = "longToMoney")
    @Mapping(source = "minConsume", target = "minConsumeAmount", qualifiedByName = "longToMoney")
    @Mapping(target = "templateId", source = "templateId")
    UserCoupon toEntity(UserCouponModel model);
    
    // Entity 转 Model
    @Mapping(source = "couponAmount", target = "couponValue", qualifiedByName = "moneyToLong")
    @Mapping(source = "minConsumeAmount", target = "minConsume", qualifiedByName = "moneyToLong")
    @Mapping(target = "templateId", source = "templateId")
    UserCouponModel toModel(UserCoupon entity);
    
    @Named("longToMoney")
    default Money longToMoney(Long value) {
        if (value == null) {
            return null;
        }
        return new Money(value);
    }
    
    @Named("moneyToLong")
    default Long moneyToLong(Money money) {
        if (money == null) {
            return null;
        }
        return money.getAmount();
    }
}
