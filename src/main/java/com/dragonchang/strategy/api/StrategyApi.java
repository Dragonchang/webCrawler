package com.dragonchang.strategy.api;

import com.dragonchang.domain.po.CompanyPriceRecord;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.FinanceAnalysis;
import com.dragonchang.strategy.support.StrategyFinanceDataService;
import com.dragonchang.strategy.support.StrategyMarketDataService;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StrategyApi {
    private final List<CompanyStock> universe;
    private final StrategyFinanceDataService financeDataService;
    private final StrategyMarketDataService marketDataService;
    private final Map<Integer, FinanceAnalysis> financeMap;

    public StrategyApi(List<CompanyStock> universe,
                       StrategyFinanceDataService financeDataService,
                       StrategyMarketDataService marketDataService,
                       Map<Integer, FinanceAnalysis> financeMap) {
        this.universe = universe;
        this.financeDataService = financeDataService;
        this.marketDataService = marketDataService;
        this.financeMap = financeMap;
    }

    public List<CompanyStock> stocks() {
        return universe;
    }

    public CompanyStock stock(Integer stockId) {
        for (CompanyStock stock : universe) {
            if (stockId.equals(stock.getId())) {
                return stock;
            }
        }
        return null;
    }

    public List<CompanyStock> stocksByBk(String bkName) {
        List<CompanyStock> ret = new ArrayList<CompanyStock>();
        for (CompanyStock stock : universe) {
            if (stock != null) {
                ret.add(stock);
            }
        }
        return ret;
    }

    public FinanceAnalysis finance(Integer stockId) {
        if (stockId == null) {
            return null;
        }
        FinanceAnalysis finance = financeMap == null ? null : financeMap.get(stockId);
        if (finance != null) {
            return finance;
        }
        return financeDataService.latestFinance(stockId);
    }

    public List<CompanyPriceRecord> prices(Integer stockId, Integer days) {
        return marketDataService.latestPrices(stockId, days);
    }

    public BigDecimal ma(Integer stockId, Integer days) {
        return marketDataService.movingAverage(stockId, days);
    }

    public BigDecimal ma(CompanyStock stock, Integer days) {
        if (stock == null) {
            return null;
        }
        return ma(stock.getId(), days);
    }

    public List<CompanyStock> filter_paused_stock(List<CompanyStock> stocks) {
        List<CompanyStock> ret = new ArrayList<CompanyStock>();
        if (stocks == null) {
            return ret;
        }
        for (CompanyStock stock : stocks) {
            if (!isPausedStock(stock)) {
                ret.add(stock);
            }
        }
        return ret;
    }

    public List<CompanyStock> filter_st_stock(List<CompanyStock> stocks) {
        List<CompanyStock> ret = new ArrayList<CompanyStock>();
        if (stocks == null) {
            return ret;
        }
        for (CompanyStock stock : stocks) {
            if (!isStStock(stock)) {
                ret.add(stock);
            }
        }
        return ret;
    }

    public List<CompanyStock> filter_new_stock(List<CompanyStock> stocks, Integer days) {
        List<CompanyStock> ret = new ArrayList<CompanyStock>();
        if (stocks == null) {
            return ret;
        }
        int minDays = days == null || days <= 0 ? 60 : days;
        for (CompanyStock stock : stocks) {
            if (!isNewStock(stock, minDays)) {
                ret.add(stock);
            }
        }
        return ret;
    }

    public boolean isPausedStock(CompanyStock stock) {
        if (stock == null) {
            return true;
        }
        if (stock.getLastPrice() == null || stock.getLastPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }
        String volume = StringUtils.trimToEmpty(stock.getDtcjl());
        String amount = stock.getDtcjje() == null ? "" : stock.getDtcjje().stripTrailingZeros().toPlainString();
        if (isZeroLike(volume) && isZeroLike(amount)) {
            return true;
        }
        return false;
    }

    public boolean isStStock(CompanyStock stock) {
        if (stock == null || StringUtils.isBlank(stock.getName())) {
            return false;
        }
        String name = StringUtils.upperCase(StringUtils.deleteWhitespace(stock.getName()));
        return name.contains("ST") || name.contains("*ST") || name.contains("退");
    }

    public boolean isNewStock(CompanyStock stock, Integer days) {
        if (stock == null) {
            return false;
        }
        int minDays = days == null || days <= 0 ? 60 : days;
        LocalDateTime marketTime = stock.getMarketTime();
        if (marketTime == null) {
            return false;
        }
        long listedDays = ChronoUnit.DAYS.between(marketTime.toLocalDate(), LocalDate.now());
        return listedDays < minDays;
    }

    public BigDecimal score(CompanyStock stock) {
        BigDecimal score = BigDecimal.ZERO;
        if (stock.getTotalCapitalization() != null) {
            score = score.add(stock.getTotalCapitalization().min(new BigDecimal("500")));
        }
        if (stock.getLastPrice() != null) {
            score = score.add(stock.getLastPrice().min(new BigDecimal("100")));
        }
        FinanceAnalysis finance = finance(stock.getId());
        if (finance != null && finance.getTotalIncome() != null) {
            score = score.add(finance.getTotalIncome().divide(new BigDecimal("100000000"), 2, RoundingMode.HALF_UP)
                    .min(new BigDecimal("300")));
        }
        return score.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        return new BigDecimal(String.valueOf(value));
    }

    private boolean isZeroLike(String value) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        String normalized = StringUtils.replaceEach(value.trim(), new String[]{",", "手", "股", "元"}, new String[]{"", "", "", ""});
        return StringUtils.equalsAny(normalized, "0", "0.0", "0.00", "--", "-");
    }
}
