package com.atguigu.gmall.seckill.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 优惠券领用表
 * @TableName coupon_use
 */
@TableName(value ="coupon_use")
@Data
public class CouponUse implements Serializable {
    /**
     * 主键编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 购物券编号
     */
    private Long couponId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 购物券状态（1：未使用 2：已使用）
     */
    private String couponStatus;

    /**
     * 获取时间
     */
    private Date getTime;

    /**
     * 使用时间
     */
    private Date usingTime;

    /**
     * 支付时间
     */
    private Date usedTime;

    /**
     * 过期时间
     */
    private Date expireTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CouponUse other = (CouponUse) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCouponId() == null ? other.getCouponId() == null : this.getCouponId().equals(other.getCouponId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getCouponStatus() == null ? other.getCouponStatus() == null : this.getCouponStatus().equals(other.getCouponStatus()))
            && (this.getGetTime() == null ? other.getGetTime() == null : this.getGetTime().equals(other.getGetTime()))
            && (this.getUsingTime() == null ? other.getUsingTime() == null : this.getUsingTime().equals(other.getUsingTime()))
            && (this.getUsedTime() == null ? other.getUsedTime() == null : this.getUsedTime().equals(other.getUsedTime()))
            && (this.getExpireTime() == null ? other.getExpireTime() == null : this.getExpireTime().equals(other.getExpireTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCouponId() == null) ? 0 : getCouponId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getCouponStatus() == null) ? 0 : getCouponStatus().hashCode());
        result = prime * result + ((getGetTime() == null) ? 0 : getGetTime().hashCode());
        result = prime * result + ((getUsingTime() == null) ? 0 : getUsingTime().hashCode());
        result = prime * result + ((getUsedTime() == null) ? 0 : getUsedTime().hashCode());
        result = prime * result + ((getExpireTime() == null) ? 0 : getExpireTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", couponId=").append(couponId);
        sb.append(", userId=").append(userId);
        sb.append(", orderId=").append(orderId);
        sb.append(", couponStatus=").append(couponStatus);
        sb.append(", getTime=").append(getTime);
        sb.append(", usingTime=").append(usingTime);
        sb.append(", usedTime=").append(usedTime);
        sb.append(", expireTime=").append(expireTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}