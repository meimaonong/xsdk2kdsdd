package com.art.app.payment.enums;

import com.art.app.common.enums.PaymentStatusEnum;

import java.util.Arrays;
import java.util.Objects;

public enum AlipayTradeStatusEnum {

    WAIT_BUYER_PAY("WAIT_BUYER_PAY", "交易创建,等待买家付款"),
    TRADE_CLOSED("TRADE_CLOSED", "未付款交易超时关闭,或支付完成后全额退款"),
    TRADE_SUCCESS("TRADE_SUCCESS", "交易支付成功"),
    TRADE_FINISHED("TRADE_FINISHED", "交易结束,不可退款"),
    ;

    private String tradeStatus;
    private String desc;

    AlipayTradeStatusEnum(String tradeStatus, String desc) {
        this.tradeStatus = tradeStatus;
        this.desc = desc;
    }

    public static AlipayTradeStatusEnum convertByOrderStatus(String tradeStatus) {
        return Arrays.stream(AlipayTradeStatusEnum.values()).filter(statusEnum -> {
            return Objects.equals(statusEnum.getTradeStatus(), tradeStatus);
        }).findFirst().orElse(null);
    }

    public static PaymentStatusEnum convertAlipayTradeStatusToPaymentStatus(AlipayTradeStatusEnum alipayTradeStatusEnum) {
        PaymentStatusEnum statusEnum = null;
        if (TRADE_SUCCESS.equals(alipayTradeStatusEnum)
                || TRADE_FINISHED.equals(alipayTradeStatusEnum)) {
            // 支付成功
            statusEnum = PaymentStatusEnum.PAY_SUCCESS;
        } else if (WAIT_BUYER_PAY.equals(alipayTradeStatusEnum)) {
            // 未支付
            statusEnum = PaymentStatusEnum.PAYING;
        } else if (TRADE_CLOSED.equals(alipayTradeStatusEnum)) {
            // 支付失败
            statusEnum = PaymentStatusEnum.PAY_FAILED;
        } else {
            statusEnum = PaymentStatusEnum.UNKNOWN;
        }
        return statusEnum;
    }

    public static RefundStatusEnum convertAlipayTradeStatusToRefundStatus(AlipayTradeStatusEnum alipayTradeStatusEnum) {
        RefundStatusEnum statusEnum = null;
        if (TRADE_FINISHED.equals(alipayTradeStatusEnum)) {
            // 退款失败
            statusEnum = RefundStatusEnum.REFUND_FAILED;
        } else if (TRADE_CLOSED.equals(alipayTradeStatusEnum)) {
            // 退款成功
            statusEnum = RefundStatusEnum.REFUND_SUCCESS;
        } else {
            statusEnum = RefundStatusEnum.REFUNDING;
        }
        return statusEnum;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public String getDesc() {
        return desc;
    }
}
