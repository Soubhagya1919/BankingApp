package com.soubhagya.banking.controller;

import com.soubhagya.banking.dto.TransactionDto;
import com.soubhagya.banking.dto.TransferFundDto;
import com.soubhagya.banking.service.AccountService;
import com.soubhagya.banking.dto.AccountDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //Add Account REST API
    @PostMapping
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto){
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id){
        AccountDto accountDto = accountService.getAccountById(id);
        return ResponseEntity.ok(accountDto);
    }

    @PutMapping("{id}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Long id,
                                              @RequestBody Map<String, Double> request){
        Double amount = request.get("amount");
        AccountDto accountDto = accountService.deposit(id, amount);
        return ResponseEntity.ok(accountDto);
    }

    @PutMapping("{id}/withdraw")
    public  ResponseEntity<AccountDto> withdraw(@PathVariable Long id,
                                                @RequestBody Map<String, Double> request){
        double amount = request.get("amount");
        AccountDto accountDto = accountService.withdraw(id, amount);
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        var allAccounts = accountService.getAllAccounts();
        return ResponseEntity.ok(allAccounts);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        accountService.deleteAccount(id);
        return  ResponseEntity.ok("Account is deleted successfully!!");
    }

    @PostMapping("/transfer")
    public  ResponseEntity<String> transferFund(@RequestBody TransferFundDto transferFundDto){
        accountService.transferFunds(transferFundDto);
        return ResponseEntity.ok("Transfer Successful");
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDto>> fetchAccountTransaction(@PathVariable("id") Long accountId){
        var accountTransactions = accountService.getAccountTransactions(accountId);
        return ResponseEntity.ok(accountTransactions);
    }
}
