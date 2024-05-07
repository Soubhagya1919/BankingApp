package com.soubhagya.banking.enums;

import lombok.Getter;

@Getter
public enum TransactionType {
    DEPOSIT("DEPOSIT"),
    WITHDRAW("WITHDRAW"),
    TRANSFER("TRANSFER");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

}
