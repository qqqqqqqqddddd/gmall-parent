package com.atguigu.gmall.seckill.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 优惠规则
 * @TableName activity_rule
 */
@TableName(value ="activity_rule")
@Data
public class ActivityRule implements Serializable {
    /**
     * 主键编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 活动Id
     */
    private Integer activityId;

    /**
     * 满减金额
     */
    private BigDecimal conditionAmount;

    /**
     * 满减件数
     */
    private Long conditionNum;

    /**
     * 优惠金额
     */
    private BigDecimal benefitAmount;

    /**
     * 优惠折扣
     */
    private BigDecimal benefitDiscount;

    /**
     * 优惠级别
     */
    private Long benefitLevel;

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
        ActivityRule other = (ActivityRule) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getActivityId() == null ? other.getActivityId() == null : this.getActivityId().equals(other.getActivityId()))
            && (this.getConditionAmount() == null ? other.getConditionAmount() == null : this.getConditionAmount().equals(other.getConditionAmount()))
            && (this.getConditionNum() == null ? other.getConditionNum() == null : this.getConditionNum().equals(other.getConditionNum()))
            && (this.getBenefitAmount() == null ? other.getBenefitAmount() == null : this.getBenefitAmount().equals(other.getBenefitAmount()))
            && (this.getBenefitDiscount() == null ? other.getBenefitDiscount() == null : this.getBenefitDiscount().equals(other.getBenefitDiscount()))
            && (this.getBenefitLevel() == null ? other.getBenefitLevel() == null : this.getBenefitLevel().equals(other.getBenefitLevel()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getActivityId() == null) ? 0 : getActivityId().hashCode());
        result = prime * result + ((getConditionAmount() == null) ? 0 : getConditionAmount().hashCode());
        result = prime * result + ((getConditionNum() == null) ? 0 : getConditionNum().hashCode());
        result = prime * result + ((getBenefitAmount() == null) ? 0 : getBenefitAmount().hashCode());
        result = prime * result + ((getBenefitDiscount() == null) ? 0 : getBenefitDiscount().hashCode());
        result = prime * result + ((getBenefitLevel() == null) ? 0 : getBenefitLevel().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", activityId=").append(activityId);
        sb.append(", conditionAmount=").append(conditionAmount);
        sb.append(", conditionNum=").append(conditionNum);
        sb.append(", benefitAmount=").append(benefitAmount);
        sb.append(", benefitDiscount=").append(benefitDiscount);
        sb.append(", benefitLevel=").append(benefitLevel);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}