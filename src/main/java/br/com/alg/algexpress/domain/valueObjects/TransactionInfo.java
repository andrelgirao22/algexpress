package br.com.alg.algexpress.domain.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TransactionInfo {
    
    @Column(name = "transaction_id", length = 100)
    private String transactionId;
    
    @Column(name = "authorization_code", length = 50)
    private String authorizationCode;
    
    public static TransactionInfo empty() {
        return new TransactionInfo(null, null);
    }
    
    public static TransactionInfo of(String transactionId, String authorizationCode) {
        return new TransactionInfo(transactionId, authorizationCode);
    }
    
    public static TransactionInfo withTransactionId(String transactionId) {
        return new TransactionInfo(transactionId, null);
    }
    
    public static TransactionInfo withAuthorizationCode(String authorizationCode) {
        return new TransactionInfo(null, authorizationCode);
    }
    
    public boolean hasTransactionId() {
        return transactionId != null && !transactionId.trim().isEmpty();
    }
    
    public boolean hasAuthorizationCode() {
        return authorizationCode != null && !authorizationCode.trim().isEmpty();
    }
    
    public boolean isEmpty() {
        return !hasTransactionId() && !hasAuthorizationCode();
    }
    
    public boolean isComplete() {
        return hasTransactionId() && hasAuthorizationCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionInfo that = (TransactionInfo) o;
        return Objects.equals(transactionId, that.transactionId) && 
               Objects.equals(authorizationCode, that.authorizationCode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(transactionId, authorizationCode);
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "No transaction info";
        }
        StringBuilder sb = new StringBuilder();
        if (hasTransactionId()) {
            sb.append("TxID: ").append(transactionId);
        }
        if (hasAuthorizationCode()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Auth: ").append(authorizationCode);
        }
        return sb.toString();
    }
}