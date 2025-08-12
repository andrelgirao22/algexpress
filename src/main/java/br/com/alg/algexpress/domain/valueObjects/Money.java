package br.com.alg.algexpress.domain.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Money {
    
    @Column(precision = 8, scale = 2)
    private BigDecimal amount;
    
    public static Money of(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        return new Money(amount.setScale(2, RoundingMode.HALF_UP));
    }
    
    public static Money of(double amount) {
        return of(BigDecimal.valueOf(amount));
    }
    
    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }
    
    public Money add(Money other) {
        if (other == null) {
            return this;
        }
        return new Money(this.amount.add(other.amount));
    }
    
    public Money subtract(Money other) {
        if (other == null) {
            return this;
        }
        return new Money(this.amount.subtract(other.amount));
    }
    
    public Money multiply(BigDecimal factor) {
        if (factor == null) {
            throw new IllegalArgumentException("Factor cannot be null");
        }
        return new Money(this.amount.multiply(factor).setScale(2, RoundingMode.HALF_UP));
    }
    
    public Money multiply(int factor) {
        return multiply(BigDecimal.valueOf(factor));
    }
    
    public boolean isGreaterThan(Money other) {
        if (other == null) {
            return true;
        }
        return this.amount.compareTo(other.amount) > 0;
    }
    
    public boolean isGreaterThanOrEqual(Money other) {
        if (other == null) {
            return true;
        }
        return this.amount.compareTo(other.amount) >= 0;
    }
    
    public boolean isLessThan(Money other) {
        if (other == null) {
            return false;
        }
        return this.amount.compareTo(other.amount) < 0;
    }
    
    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
    
    @Override
    public String toString() {
        return "R$ " + amount.toString();
    }
}