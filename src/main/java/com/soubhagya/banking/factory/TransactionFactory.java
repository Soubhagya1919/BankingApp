package com.soubhagya.banking.factory;

import com.soubhagya.banking.entity.Transaction;
import com.soubhagya.banking.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionFactory {
    public Transaction createTransaction(Long accountId, double amount, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType.getValue());
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }
}
