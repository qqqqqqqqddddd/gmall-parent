package com.atguigu.gmall.seckill.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 优惠券表
 * @TableName coupon_info
 */
@TableName(value ="coupon_info")
@Data
public class CouponInfo implements Serializable {
    /**
     * 购物券编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 购物券名称
     */
    private String couponName;

    /**
     * 购物券类型 1 现金券 2 折扣券 3 满减券 4 满件打折券
     */
    private String couponType;

    /**
     * 满额数（3）
     */
    private BigDecimal conditionAmount;

    /**
     * 满件数（4）
     */
    private Long conditionNum;

    /**
     * 活动Id
     */
    private Long activityId;

    /**
     * 减金额（1 3）
     */
    private BigDecimal benefitAmount;

    /**
     * 折扣（2 4）
     */
    private BigDecimal benefitDiscount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 范围类型 1、商品(spuid) 2、品类(三级分类id) 3、品牌(tmid)
     */
    private String rangeType;

    /**
     * 最多领用次数
     */
    private Integer limitNum;

    /**
     * 已领用次数
     */
    private Integer takenCount;

    /**
     * 可以领取的开始日期
     */
    private Date startTime;

    /**
     * 可以领取的结束日期
     */
    private Date endTime;

    /**
     * 修改时间
     */
    private Date operateTime;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 范围描述
     */
    private String rangeDesc;

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
        CouponInfo other = (CouponInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCouponName() == null ? other.getCouponName() == null : this.getCouponName().equals(other.getCouponName()))
            && (this.getCouponType() == null ? other.getCouponType() == null : this.getCouponType().equals(other.getCouponType()))
            && (this.getConditionAmount() == null ? other.getConditionAmount() == null : this.getConditionAmount().equals(other.getConditionAmount()))
            && (this.getConditionNum() == null ? other.getConditionNum() == null : this.getConditionNum().equals(other.getConditionNum()))
            && (this.getActivityId() == null ? other.getActivityId() == null : this.getActivityId().equals(other.getActivityId()))
            && (this.getBenefitAmount() == null ? other.getBenefitAmount() == null : this.getBenefitAmount().equals(other.getBenefitAmount()))
            && (this.getBenefitDiscount() == null ? other.getBenefitDiscount() == null : this.getBenefitDiscount().equals(other.getBenefitDiscount()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getRangeType() == null ? other.getRangeType() == null : this.getRangeType().equals(other.getRangeType()))
            && (this.getLimitNum() == null ? other.getLimitNum() == null : this.getLimitNum().equals(other.getLimitNum()))
            && (this.getTakenCount() == null ? other.getTakenCount() == null : this.getTakenCount().equals(other.getTakenCount()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getOperateTime() == null ? other.getOperateTime() == null : this.getOperateTime().equals(other.getOperateTime()))
            && (this.getExpireTime() == null ? other.getExpireTime() == null : this.getExpireTime().equals(other.getExpireTime()))
            && (this.getRangeDesc() == null ? other.getRangeDesc() == null : this.getRangeDesc().equals(other.getRangeDesc()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCouponName() == null) ? 0 : getCouponName().hashCode());
        result = prime * result + ((getCouponType() == null) ? 0 : getCouponType().hashCode());
        result = prime * result + ((getConditionAmount() == null) ? 0 : getConditionAmount().hashCode());
        result = prime * result + ((getConditionNum() == null) ? 0 : getConditionNum().hashCode());
        result = prime * result + ((getActivityId() == null) ? 0 : getActivityId().hashCode());
        result = prime * result + ((getBenefitAmount() == null) ? 0 : getBenefitAmount().hashCode());
        result = prime * result + ((getBenefitDiscount() == null) ? 0 : getBenefitDiscount().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getRangeType() == null) ? 0 : getRangeType().hashCode());
        result = prime * result + ((getLimitNum() == null) ? 0 : getLimitNum().hashCode());
        result = prime * result + ((getTakenCount() == null) ? 0 : getTakenCount().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getOperateTime() == null) ? 0 : getOperateTime().hashCode());
        result = prime * result + ((getExpireTime() == null) ? 0 : getExpireTime().hashCode());
        result = prime * result + ((getRangeDesc() == null) ? 0 : getRangeDesc().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", couponName=").append(couponName);
        sb.append(", couponType=").append(couponType);
        sb.append(", conditionAmount=").append(conditionAmount);
        sb.append(", conditionNum=").append(conditionNum);
        sb.append(", activityId=").append(activityId);
        sb.append(", benefitAmount=").append(benefitAmount);
        sb.append(", benefitDiscount=").append(benefitDiscount);
        sb.append(", createTime=").append(createTime);
        sb.append(", rangeType=").append(rangeType);
        sb.append(", limitNum=").append(limitNum);
        sb.append(", takenCount=").append(takenCount);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", operateTime=").append(operateTime);
        sb.append(", expireTime=").append(expireTime);
        sb.append(", rangeDesc=").append(rangeDesc);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}