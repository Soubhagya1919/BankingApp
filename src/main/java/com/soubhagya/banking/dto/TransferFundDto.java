package com.soubhagya.banking.dto;

public record TransferFundDto(Long fromAccountId,
                             Long toAccountId,
                             double amount) {
}
