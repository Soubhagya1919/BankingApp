package com.soubhagya.banking.mapper;

import com.soubhagya.banking.dto.TransactionDto;
import com.soubhagya.banking.entity.Transaction;

public class TransactionMapper {
    public static TransactionDto convertEntityToDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTimestamp()
        );
    }

    public static Transaction convertEntityToDto(TransactionDto transactionDto) {
        return new Transaction(
                transactionDto.id(),
                transactionDto.accountId(),
                transactionDto.amount(),
                transactionDto.transactionType(),
                transactionDto.timestamp()
        );
    }
}
