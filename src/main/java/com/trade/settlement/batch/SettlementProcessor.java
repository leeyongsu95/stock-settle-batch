package com.trade.settlement.batch;

import com.trade.settlement.dto.ExecutionSettlementDto;
import com.trade.settlement.dto.SettlementInsertDto;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 수수료/세금 계산.
 * 매수: 수수료만 / 매도: 수수료 + 증권거래세
 * 수수료율은 회원별 DB 설정값 사용 (t_member.commission_rate)
 */
public class SettlementProcessor implements ItemProcessor<ExecutionSettlementDto, SettlementInsertDto> {

    private static final BigDecimal DEFAULT_COMMISSION_RATE = new BigDecimal("0.000150");
    private static final BigDecimal TAX_RATE = new BigDecimal("0.0023");  // 법정 증권거래세 (매도만)

    @Override
    public SettlementInsertDto process(ExecutionSettlementDto exec) {
        BigDecimal execAmt = exec.getExecAmt();
        BigDecimal rate = exec.getCommissionRate() != null ? exec.getCommissionRate() : DEFAULT_COMMISSION_RATE;
        BigDecimal commission = execAmt.multiply(rate).setScale(2, RoundingMode.FLOOR);
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal balanceChange;

        boolean isSell = "SELL".equals(exec.getOrderTypeCd());

        if (isSell) {
            tax = execAmt.multiply(TAX_RATE).setScale(2, RoundingMode.FLOOR);
            balanceChange = execAmt.subtract(commission).subtract(tax);    // 매도 입금
        } else {
            balanceChange = commission.negate();                            // 매수 수수료 추가 차감
        }

        BigDecimal netAmt = execAmt.subtract(commission).subtract(tax);

        SettlementInsertDto dto = new SettlementInsertDto();
        dto.setExecSeq(exec.getExecSeq());
        dto.setOrderId(exec.getOrderId());
        dto.setMemberKey(exec.getMemberKey());
        dto.setSettlementTypeCd("NORMAL");
        dto.setSettlementAmt(execAmt);
        dto.setCommissionRate(rate);
        dto.setCommission(commission);
        dto.setTax(tax);
        dto.setNetAmt(netAmt);
        dto.setLeverageYn(exec.getLeverageYn());
        dto.setBalanceChange(balanceChange);

        return dto;
    }
}
