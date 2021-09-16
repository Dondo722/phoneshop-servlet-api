package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.*;

public class Price {
    private BigDecimal currentPrice;
    private Date priceBeginDate;
    private final Currency currency;
    private final Map<Date,BigDecimal> priceHistory;

    public Price(BigDecimal currentPrice, Date priceBeginDate, Currency currency) {
        this.currentPrice = currentPrice;
        this.priceBeginDate = priceBeginDate;
        this.currency = currency;
        priceHistory = new HashMap<>();
        //priceHistory.put(priceBeginDate,currentPrice);
    }

    public void changeCurrentPrice(BigDecimal newPrice, Date priceBeginDate) {
        this.currentPrice = newPrice;
        this.priceBeginDate = priceBeginDate;
        priceHistory.put(priceBeginDate,currentPrice);
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public Date getPriceBeginDate() {
        return priceBeginDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Map<Date, BigDecimal> getPriceHistory() {
        return priceHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(currentPrice, price.currentPrice) &&
                Objects.equals(priceBeginDate, price.priceBeginDate) && Objects.equals(currency, price.currency) &&
                Objects.equals(priceHistory, price.priceHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPrice, priceBeginDate, currency, priceHistory);
    }
}
