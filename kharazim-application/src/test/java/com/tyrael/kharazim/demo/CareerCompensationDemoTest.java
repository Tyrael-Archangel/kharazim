package com.tyrael.kharazim.demo;

import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.tyrael.kharazim.demo.CareerCompensationDemoTest.CompensationType.*;

/**
 * @author Tyrael Archangel
 * @since 2025/3/27
 */
public class CareerCompensationDemoTest {

    @Test
    public void careerCompensation() {
        List<Compensation> compensations = new ArrayList<>(getCompensations());
        compensations.sort(Comparator.comparing(Compensation::getDate).thenComparing(Compensation::getSalary));

        Map<String, CompanySummary> companySummaryMap = new HashMap<>();
        for (Compensation compensation : compensations) {
            String company = compensation.getCompany();
            CompanySummary companySummary = companySummaryMap.get(company);
            if (companySummary == null) {
                companySummaryMap.put(company, new CompanySummary());
            } else {
                companySummary.merge(new CompanySummary(compensation));
            }
        }


        int sumMonths = 0;
        BigDecimal sumCompensation = BigDecimal.ZERO;
        for (CompanySummary companySummary : companySummaryMap.values()) {
            sumMonths += companySummary.getMonths();
            sumCompensation = sumCompensation.add(companySummary.getSum());
        }
        BigDecimal finalSumCompensation = sumCompensation;
        int finalSumMonths = sumMonths;

        String summaryLineHeader = pretty("company", 12) +
                pretty("sum", 12) +
                pretty("months", 8) +
                pretty("sumSalary", 12) +
                pretty("avg", 12) +
                pretty("salaryAvg", 12) +
                pretty("compensationPercent", 24) +
                pretty("monthPercent", 16) +
                pretty("sumOther", 12);
        int lineHeaderLength = summaryLineHeader.length();
        System.out.println("=".repeat(lineHeaderLength));

        LocalDate firstDay = compensations.stream()
                .map(Compensation::getDate)
                .min(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalStateException("should not happen"));

        long untilMonths = firstDay.until(LocalDate.now(), ChronoUnit.MONTHS);
        long untilDays = firstDay.until(LocalDate.now(), ChronoUnit.DAYS);

        System.out.println("Entire career compensation:\t" + finalSumCompensation
                + ",\t\tfirst month: " + YearMonth.of(firstDay.getYear(), firstDay.getMonth())
                + ",\t\ttotal months: " + untilMonths
                + ",\t\ttotal days: " + untilDays
        );

        System.out.println("=".repeat(lineHeaderLength));
        System.out.println(summaryLineHeader);
        companySummaryMap.values()
                .stream()
                .sorted(Comparator.comparing(CompanySummary::getSum).reversed())
                .forEach(e -> {
                    BigDecimal compensationPercent = e.getSum().multiply(BigDecimal.valueOf(100))
                            .divide(finalSumCompensation, 2, RoundingMode.HALF_UP);
                    BigDecimal monthPercent = BigDecimal.valueOf(e.getMonths()).multiply(BigDecimal.valueOf(100))
                            .divide(BigDecimal.valueOf(finalSumMonths), 2, RoundingMode.HALF_UP);
                    System.out.println(pretty(e.getCompany(), 12) +
                            pretty(e.getSum(), 12) +
                            pretty(e.getMonths(), 8) +
                            pretty(e.getSumSalary(), 12) +
                            pretty(e.getAvg(), 12) +
                            pretty(e.getSalaryAvg(), 12) +
                            pretty(compensationPercent + "%", 24) +
                            pretty(monthPercent + "%", 16) +
                            pretty(e.getSumOther(), 12)
                    );
                });

        System.out.println("=".repeat(lineHeaderLength));
        String detailLineHeader = pretty("company", 12) +
                pretty("datetime", 12) +
                pretty("compensation", 16) +
                pretty("type", 26);
        System.out.println(detailLineHeader);
        for (Compensation compensation : compensations) {
            System.out.println(pretty(compensation.getCompany(), 12)
                    + pretty(compensation.getDate(), 12)
                    + pretty(compensation.getSalary(), 16)
                    + pretty(compensation.getType(), 26));
        }
        System.out.println("=".repeat(lineHeaderLength));

    }

    private String pretty(Object name, int length) {
        if (name instanceof BigDecimal decimal) {
            name = decimal.setScale(2, RoundingMode.HALF_UP);
        }
        int repeatCount = length - name.toString().length();
        int halfRepeatCount = repeatCount / 2;
        if (name instanceof BigDecimal) {
            halfRepeatCount = repeatCount - 2;
        }
        return "|" + " ".repeat(halfRepeatCount) + name + " ".repeat(repeatCount - halfRepeatCount) + "|";
    }

    private List<Compensation> getCompensations() {
        return List.of(
                new Compensation("gsi", LocalDate.of(2016, 7, 28), 1500.00, RESETTLEMENT_ALLOWANCE),
                new Compensation("gsi", LocalDate.of(2016, 8, 15), 2260.84, SALARY),
                new Compensation("gsi", LocalDate.of(2016, 9, 14), 2529.34, SALARY),
                new Compensation("gsi", LocalDate.of(2016, 10, 14), 2460.66, SALARY),
                new Compensation("gsi", LocalDate.of(2016, 11, 15), 2493.66, SALARY),
                new Compensation("gsi", LocalDate.of(2016, 12, 15), 2557.42, SALARY),
                new Compensation("gsi", LocalDate.of(2016, 12, 23), 2932.22, BONUS),
                new Compensation("gsi", LocalDate.of(2017, 1, 13), 2244.76, SALARY),
                new Compensation("gsi", LocalDate.of(2017, 2, 15), 3420.10, SALARY),
                new Compensation("gsi", LocalDate.of(2017, 3, 15), 2306.36, SALARY),
                new Compensation("gsi", LocalDate.of(2017, 4, 14), 2119.71, SALARY),
                new Compensation("gsi", LocalDate.of(2017, 5, 15), 3206.14, SALARY),


                new Compensation("systex", LocalDate.of(2017, 12, 5), 3859.11, SALARY),
                new Compensation("systex", LocalDate.of(2018, 1, 5), 4652.75, SALARY),
                new Compensation("systex", LocalDate.of(2018, 2, 5), 4652.75, SALARY),
                new Compensation("systex", LocalDate.of(2018, 2, 9), 1577.22, BONUS),
                new Compensation("systex", LocalDate.of(2018, 3, 5), 4070.75, SALARY),
                new Compensation("systex", LocalDate.of(2018, 4, 4), 4652.75, SALARY),
                new Compensation("systex", LocalDate.of(2018, 5, 4), 4652.75, SALARY),
                new Compensation("systex", LocalDate.of(2018, 6, 5), 6024.56, SALARY),
                new Compensation("systex", LocalDate.of(2018, 7, 5), 6024.56, SALARY),
                new Compensation("systex", LocalDate.of(2018, 8, 3), 6006.20, SALARY),
                new Compensation("systex", LocalDate.of(2018, 9, 5), 6006.20, SALARY),
                new Compensation("systex", LocalDate.of(2018, 10, 8), 6006.20, SALARY),
                new Compensation("systex", LocalDate.of(2018, 11, 3), 6132.96, SALARY),
                new Compensation("systex", LocalDate.of(2018, 12, 5), 6132.96, SALARY),
                new Compensation("systex", LocalDate.of(2019, 1, 4), 6132.96, SALARY),
                new Compensation("systex", LocalDate.of(2019, 2, 1), 6132.96, SALARY),
                new Compensation("systex", LocalDate.of(2019, 2, 1), 7275.00, BONUS),
                new Compensation("systex", LocalDate.of(2019, 3, 5), 6132.96, SALARY),
                new Compensation("systex", LocalDate.of(2019, 4, 4), 6132.96, SALARY),
                new Compensation("systex", LocalDate.of(2019, 5, 5), 6168.00, SALARY),
                new Compensation("systex", LocalDate.of(2019, 6, 5), 13597.92, SALARY),
                new Compensation("systex", LocalDate.of(2019, 7, 5), 7632.96, SALARY),
                new Compensation("systex", LocalDate.of(2019, 8, 5), 7417.62, SALARY),
                new Compensation("systex", LocalDate.of(2019, 9, 5), 7417.62, SALARY),
                new Compensation("systex", LocalDate.of(2019, 9, 29), 7417.62, SALARY),
                new Compensation("systex", LocalDate.of(2019, 11, 6), 7417.62, SALARY),
                new Compensation("systex", LocalDate.of(2019, 12, 5), 7417.62, SALARY),
                new Compensation("systex", LocalDate.of(2020, 1, 3), 7417.62, SALARY),
                new Compensation("systex", LocalDate.of(2020, 2, 13), 7417.62, SALARY),
                new Compensation("systex", LocalDate.of(2020, 2, 13), 23750.00, SEVERANCE_PAY),


                new Compensation("starlinke", LocalDate.of(2020, 4, 10), 5633.33, SALARY),
                new Compensation("starlinke", LocalDate.of(2020, 5, 11), 10340.15, SALARY),
                new Compensation("starlinke", LocalDate.of(2020, 6, 12), 10538.15, SALARY),
                new Compensation("starlinke", LocalDate.of(2020, 7, 10), 12073.51, SALARY),
                new Compensation("starlinke", LocalDate.of(2020, 8, 11), 12899.17, SALARY),
                new Compensation("starlinke", LocalDate.of(2020, 9, 10), 12972.76, SALARY),
                new Compensation("starlinke", LocalDate.of(2020, 10, 13), 16347.39, SALARY),
                new Compensation("starlinke", LocalDate.of(2020, 11, 11), 16728.07, SALARY),
                new Compensation("starlinke", LocalDate.of(2020, 12, 10), 14873.58, SALARY),
                new Compensation("starlinke", LocalDate.of(2021, 1, 8), 16949.76, SALARY),
                new Compensation("starlinke", LocalDate.of(2021, 2, 7), 26667.66, SALARY),
                new Compensation("starlinke", LocalDate.of(2021, 3, 10), 20154.07, SALARY),
                new Compensation("starlinke", LocalDate.of(2021, 4, 10), 13213.30, SALARY),
                new Compensation("starlinke", LocalDate.of(2021, 5, 10), 26506.30, SALARY),
                new Compensation("starlinke", LocalDate.of(2021, 6, 10), 17600.40, SALARY),
                new Compensation("starlinke", LocalDate.of(2021, 7, 9), 17519.41, SALARY),
                new Compensation("starlinke", LocalDate.of(2021, 8, 10), 17492.40, SALARY),
                new Compensation("starlinke", LocalDate.of(2021, 9, 10), 17654.41, SALARY),
                new Compensation("starlinke", LocalDate.of(2021, 10, 9), 17519.40, SALARY),
                new Compensation("starlinke", LocalDate.of(2021, 11, 10), 22297.45, SALARY),
                new Compensation("starlinke", LocalDate.of(2021, 11, 10), 59274.10, SEVERANCE_PAY),


                new Compensation("zhican", LocalDate.of(2021, 12, 10), 10026.36, SALARY),
                new Compensation("zhican", LocalDate.of(2022, 1, 10), 14966.36, SALARY),
                new Compensation("zhican", LocalDate.of(2022, 1, 27), 2168.55, BONUS),
                new Compensation("zhican", LocalDate.of(2022, 2, 10), 15294.12, SALARY),
                new Compensation("zhican", LocalDate.of(2022, 3, 10), 16519.40, SALARY),
                new Compensation("zhican", LocalDate.of(2022, 4, 8), 19097.07, SALARY),
                new Compensation("zhican", LocalDate.of(2022, 5, 10), 18191.75, SALARY),
                new Compensation("zhican", LocalDate.of(2022, 6, 10), 18098.15, SALARY),
                new Compensation("zhican", LocalDate.of(2022, 7, 9), 18192.65, SALARY),
                new Compensation("zhican", LocalDate.of(2022, 8, 10), 17916.82, SALARY),
                new Compensation("zhican", LocalDate.of(2022, 9, 9), 18128.86, SALARY),
                new Compensation("zhican", LocalDate.of(2022, 10, 10), 14438.86, SALARY),
                new Compensation("zhican", LocalDate.of(2022, 11, 11), 18038.86, SALARY),
                new Compensation("zhican", LocalDate.of(2022, 12, 9), 17501.59, SALARY),
                new Compensation("zhican", LocalDate.of(2023, 1, 10), 16590.09, SALARY),
                new Compensation("zhican", LocalDate.of(2023, 1, 18), 1940.00, BONUS),
                new Compensation("zhican", LocalDate.of(2023, 2, 10), 19037.47, SALARY),
                new Compensation("zhican", LocalDate.of(2023, 3, 10), 19037.47, SALARY),
                new Compensation("zhican", LocalDate.of(2023, 4, 10), 18518.43, SALARY),
                new Compensation("zhican", LocalDate.of(2023, 5, 10), 18097.74, SALARY),
                new Compensation("zhican", LocalDate.of(2023, 6, 9), 17951.18, SALARY),
                new Compensation("zhican", LocalDate.of(2023, 7, 10), 18024.46, SALARY),
                new Compensation("zhican", LocalDate.of(2023, 8, 10), 18021.31, SALARY),
                new Compensation("zhican", LocalDate.of(2023, 9, 8), 18007.35, SALARY),
                new Compensation("zhican", LocalDate.of(2023, 10, 10), 18008.08, SALARY),
                new Compensation("zhican", LocalDate.of(2023, 11, 10), 17942.35, SALARY),
                new Compensation("zhican", LocalDate.of(2023, 12, 8), 16562.74, SALARY),
                new Compensation("zhican", LocalDate.of(2024, 1, 10), 16562.73, SALARY),
                new Compensation("zhican", LocalDate.of(2024, 2, 2), 19019.82, SALARY),
                new Compensation("zhican", LocalDate.of(2024, 2, 2), 1940.00, SALARY),
                new Compensation("zhican", LocalDate.of(2024, 3, 8), 19019.81, SALARY),
                new Compensation("zhican", LocalDate.of(2024, 4, 10), 18504.60, SALARY),
                new Compensation("zhican", LocalDate.of(2024, 5, 10), 18008.08, SALARY),
                new Compensation("zhican", LocalDate.of(2024, 6, 7), 50755.24, SEVERANCE_PAY),
                new Compensation("zhican", LocalDate.of(2024, 7, 10), 26407.48, SALARY),


                new Compensation("starlinke", LocalDate.of(2024, 8, 9), 13713.32, SALARY),
                new Compensation("starlinke", LocalDate.of(2024, 9, 10), 17974.72, SALARY),
                new Compensation("starlinke", LocalDate.of(2024, 10, 10), 17974.72, SALARY),
                new Compensation("starlinke", LocalDate.of(2024, 11, 8), 17417.79, SALARY),
                new Compensation("starlinke", LocalDate.of(2024, 12, 10), 17038.40, SALARY),
                new Compensation("starlinke", LocalDate.of(2025, 1, 10), 17974.72, SALARY),
                new Compensation("starlinke", LocalDate.of(2025, 2, 10), 17974.72, SALARY),
                new Compensation("starlinke", LocalDate.of(2025, 3, 10), 17685.76, SALARY)

        );
    }

    enum CompensationType {
        /**
         * 工资
         */
        SALARY,
        /**
         * 奖金
         */
        BONUS,
        /**
         * 离职补偿
         */
        SEVERANCE_PAY,
        /**
         * 安置金
         */
        RESETTLEMENT_ALLOWANCE
    }

    @Getter
    private static class Compensation {

        private final String company;
        private final LocalDate date;
        private final BigDecimal salary;
        private final CompensationType type;

        private Compensation(String company, LocalDate date, double salary, CompensationType type) {
            this.company = company;
            this.date = date;
            this.salary = BigDecimal.valueOf(salary);
            this.type = type;
        }

    }

    @Data
    private class CompanySummary {
        private String company;
        private int months;
        private BigDecimal sum;
        private BigDecimal sumSalary;
        private BigDecimal sumOther;

        CompanySummary() {
            this.months = 0;
            this.sum = BigDecimal.ZERO;
            this.sumSalary = BigDecimal.ZERO;
            this.sumOther = BigDecimal.ZERO;
        }

        CompanySummary(Compensation compensation) {
            this.company = compensation.getCompany();
            if (SALARY.equals(compensation.getType())) {
                this.months = 1;
                this.sumSalary = compensation.getSalary();
                this.sumOther = BigDecimal.ZERO;
            } else {
                this.months = 0;
                this.sumSalary = BigDecimal.ZERO;
                this.sumOther = compensation.getSalary();
            }
            this.sum = compensation.getSalary();
        }

        BigDecimal getAvg() {
            return sum.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        }

        BigDecimal getSalaryAvg() {
            return sumSalary.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        }

        void merge(CompanySummary other) {
            if (this.company == null) {
                this.company = other.company;
            }
            this.months += other.months;
            this.sum = this.sum.add(other.sum);
            this.sumOther = this.sumOther.add(other.sumOther);
            this.sumSalary = this.sumSalary.add(other.sumSalary);
        }

        @Override
        public String toString() {
            return "{" +
                    "company=" + pretty(company, 15) +
                    ", months=" + pretty(months, 6) +
                    ", sum=" + pretty(sum, 12) +
                    ", sumSalary=" + pretty(sumSalary, 12) +
                    ", sumOther=" + pretty(sumOther, 12) +
                    ", avg=" + pretty(getAvg(), 12) +
                    ", salaryAvg=" + pretty(getSalaryAvg(), 12) +
                    '}';
        }

    }

}

