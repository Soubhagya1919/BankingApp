package com.soubhagya.banking.mapper;

import com.soubhagya.banking.entity.Account;
import com.soubhagya.banking.dto.AccountDto;

public class AccountMapper {
    //
    public static Account mapToAccount(AccountDto accountDto){
        return new Account(
                accountDto.id(),
                accountDto.accountHolderName(),
                accountDto.balance()
        );
    }

    public static  AccountDto mapToAccountDto(Account account){
        return new AccountDto(
                account.getId(),
                account.getAccountHolderName(),
                account.getBalance()
        );
    }
}
