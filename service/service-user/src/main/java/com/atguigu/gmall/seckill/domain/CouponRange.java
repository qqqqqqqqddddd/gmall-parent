package com.atguigu.gmall.seckill.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 优惠券范围表
 * @TableName coupon_range
 */
@TableName(value ="coupon_range")
@Data
public class CouponRange implements Serializable {
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
     * 范围类型 1、商品(spuid) 2、品类(三级分类id) 3、品牌(tmid)
     */
    private String rangeType;

    /**
     * 范围类型对应的Id
     */
    private Long rangeId;

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
        CouponRange other = (CouponRange) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCouponId() == null ? other.getCouponId() == null : this.getCouponId().equals(other.getCouponId()))
            && (this.getRangeType() == null ? other.getRangeType() == null : this.getRangeType().equals(other.getRangeType()))
            && (this.getRangeId() == null ? other.getRangeId() == null : this.getRangeId().equals(other.getRangeId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCouponId() == null) ? 0 : getCouponId().hashCode());
        result = prime * result + ((getRangeType() == null) ? 0 : getRangeType().hashCode());
        result = prime * result + ((getRangeId() == null) ? 0 : getRangeId().hashCode());
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
        sb.append(", rangeType=").append(rangeType);
        sb.append(", rangeId=").append(rangeId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}