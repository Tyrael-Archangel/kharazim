package com.tyrael.kharazim.demo;

import com.tyrael.kharazim.common.dto.Pair;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/10/15
 */
public class CalculateHousingProvidentFundDemoTest {

    @Test
    public void calculateHousingProvidentFundDemo() {

        List<Pair<YearMonth, Integer>> from_amounts = List.of(
                Pair.of(YearMonth.of(2022, 5), 178),
                Pair.of(YearMonth.of(2023, 1), 210),
                Pair.of(YearMonth.of(2024, 7), 2400)
        );

        List<Pair<YearMonth, Integer>> out_amounts = List.of(
                Pair.of(YearMonth.of(2023, 7), 3100)
        );

        List<Pair<YearMonth, Integer>> interests_amounts = List.of(
                Pair.of(YearMonth.of(2022, 7), 175),
                Pair.of(YearMonth.of(2023, 7), 25),
                Pair.of(YearMonth.of(2024, 7), 22)
        );

        YearMonth end = YearMonth.of(2026, 4);

        List<YearMonthRecord> yearMonthRecords = new ArrayList<>();
        for (int i = 0; i < from_amounts.size(); i++) {
            Pair<YearMonth, Integer> from_amount = from_amounts.get(i);
            YearMonth from = from_amount.left();
            Integer amount = from_amount.right();
            YearMonth nextFrom = i == from_amounts.size() - 1
                    ? end.plusMonths(1)
                    : from_amounts.get(i + 1).left();
            for (YearMonth j = from; j.isBefore(nextFrom); j = j.plusMonths(1)) {
                yearMonthRecords.add(YearMonthRecord.builder()
                        .yearMonth(j)
                        .inAmount(amount)
                        .outAmount(0)
                        .deductAmount(0)
                        .coefficient(0.9)
                        .build());
            }
        }

        int balanceAmount = 190 - 178;
        for (int i = 0; i < yearMonthRecords.size(); i++) {
            YearMonthRecord yearMonthRecord = yearMonthRecords.get(i);
            yearMonthRecord.setI(yearMonthRecords.size() - i);

            for (Pair<YearMonth, Integer> outAmountPair : out_amounts) {
                if (yearMonthRecord.getYearMonth().equals(outAmountPair.left())) {
                    yearMonthRecord.setOutAmount(outAmountPair.right());
                }
            }

            balanceAmount += (yearMonthRecord.getInAmount() - yearMonthRecord.getOutAmount());
            for (Pair<YearMonth, Integer> interestsAmountPair : interests_amounts) {
                if (yearMonthRecord.getYearMonth().equals(interestsAmountPair.left())) {
                    balanceAmount += interestsAmountPair.right();
                }
            }
            yearMonthRecord.setBalanceAmount(balanceAmount);

        }

        int needDeductAmount = 0;
        for (int i = yearMonthRecords.size() - 1; i >= 0; i--) {
            YearMonthRecord yearMonthRecord = yearMonthRecords.get(i);
            Integer outAmount = yearMonthRecord.getOutAmount();
            if (outAmount > 0) {
                needDeductAmount += outAmount;
            }

            if (needDeductAmount > 0) {
                Integer inAmount = yearMonthRecord.getInAmount();
                if (needDeductAmount > inAmount) {
                    yearMonthRecord.setDeductAmount(inAmount);
                    needDeductAmount -= inAmount;
                } else {
                    yearMonthRecord.setDeductAmount(needDeductAmount);
                    needDeductAmount = 0;
                }
            }

            System.out.println(yearMonthRecord);
        }

        BigDecimal finalAmount = yearMonthRecords.stream()
                .map(YearMonthRecord::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        System.out.println("最终可贷金额: " + finalAmount);

    }

    @Data
    @Builder
    static class YearMonthRecord {

        private YearMonth yearMonth;

        private int i;
        /**
         * 缴存入账金额
         */
        private Integer inAmount;
        /**
         * 提取金额
         */
        private Integer outAmount;
        /**
         * 当月应被抵扣的提取额
         */
        private Integer deductAmount;
        /**
         * 系数
         */
        private double coefficient;
        private Integer balanceAmount;

        /**
         * 该月可纳入计算的额度
         */
        public Integer getCanCalAmount() {
            return inAmount - deductAmount;
        }

        /**
         * 该月贷款额度
         */
        public BigDecimal getFinalAmount() {
            return BigDecimal.valueOf(getCanCalAmount())
                    .multiply(BigDecimal.valueOf(coefficient))
                    .multiply(BigDecimal.valueOf(i));
        }

        @Override
        public String toString() {
            return "年月:" + yearMonth +
                    "\t i:" + i +
                    "\t 当月缴存入账金额:" + inAmount +
                    "\t 提取金额:" + outAmount +
                    "\t 当月应被抵扣的提取额:" + deductAmount +
                    "\t 该月可纳入计算的额度:" + getCanCalAmount() +
                    "\t 系数:" + coefficient +
                    "\t 该月贷款额度:" + getFinalAmount() +
                    "\t 该月结存:" + balanceAmount;
        }
    }

}
