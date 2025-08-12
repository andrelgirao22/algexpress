package br.com.alg.algexpress.domain.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PaymentMethod {
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentType type;
    
    public enum PaymentType {
        CASH("Dinheiro", true, false, "Pagamento em espécie"),
        CREDIT_CARD("Cartão de Crédito", false, true, "Cartão de crédito"),
        DEBIT_CARD("Cartão de Débito", false, true, "Cartão de débito"),
        PIX("PIX", false, true, "Transferência instantânea PIX"),
        MEAL_VOUCHER("Vale Refeição", false, false, "Vale refeição"),
        FOOD_VOUCHER("Vale Alimentação", false, false, "Vale alimentação");
        
        private final String description;
        private final boolean requiresChange;
        private final boolean requiresAuthorization;
        private final String displayName;
        
        PaymentType(String description, boolean requiresChange, boolean requiresAuthorization, String displayName) {
            this.description = description;
            this.requiresChange = requiresChange;
            this.requiresAuthorization = requiresAuthorization;
            this.displayName = displayName;
        }
        
        public String getDescription() {
            return description;
        }
        
        public boolean requiresChange() {
            return requiresChange;
        }
        
        public boolean requiresAuthorization() {
            return requiresAuthorization;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public static PaymentMethod cash() {
        return new PaymentMethod(PaymentType.CASH);
    }
    
    public static PaymentMethod creditCard() {
        return new PaymentMethod(PaymentType.CREDIT_CARD);
    }
    
    public static PaymentMethod debitCard() {
        return new PaymentMethod(PaymentType.DEBIT_CARD);
    }
    
    public static PaymentMethod pix() {
        return new PaymentMethod(PaymentType.PIX);
    }
    
    public static PaymentMethod mealVoucher() {
        return new PaymentMethod(PaymentType.MEAL_VOUCHER);
    }
    
    public static PaymentMethod foodVoucher() {
        return new PaymentMethod(PaymentType.FOOD_VOUCHER);
    }
    
    public boolean requiresChange() {
        return type != null && type.requiresChange();
    }
    
    public boolean requiresAuthorization() {
        return type != null && type.requiresAuthorization();
    }
    
    public boolean isCash() {
        return type == PaymentType.CASH;
    }
    
    public boolean isCard() {
        return type == PaymentType.CREDIT_CARD || type == PaymentType.DEBIT_CARD;
    }
    
    public boolean isElectronic() {
        return type == PaymentType.PIX || isCard();
    }
    
    public boolean isVoucher() {
        return type == PaymentType.MEAL_VOUCHER || type == PaymentType.FOOD_VOUCHER;
    }
    
    public String getDescription() {
        return type != null ? type.getDescription() : "Método não definido";
    }
    
    public String getDisplayName() {
        return type != null ? type.getDisplayName() : "Método não definido";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethod that = (PaymentMethod) o;
        return type == that.type;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
    
    @Override
    public String toString() {
        return getDisplayName();
    }
}