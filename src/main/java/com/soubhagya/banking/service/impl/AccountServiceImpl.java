package com.soubhagya.banking.service.impl;

import com.soubhagya.banking.dto.AccountDto;
import com.soubhagya.banking.dto.TransactionDto;
import com.soubhagya.banking.dto.TransferFundDto;
import com.soubhagya.banking.entity.Account;
import com.soubhagya.banking.entity.Transaction;
import com.soubhagya.banking.enums.TransactionType;
import com.soubhagya.banking.exception.AccountException;
import com.soubhagya.banking.factory.TransactionFactory;
import com.soubhagya.banking.mapper.AccountMapper;
import com.soubhagya.banking.mapper.TransactionMapper;
import com.soubhagya.banking.repository.AccountRepository;
import com.soubhagya.banking.repository.TransactionRepository;
import com.soubhagya.banking.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final TransactionFactory transactionFactory;

    //private static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    //@Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              TransactionRepository transactionRepository, TransactionFactory transactionFactory) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionFactory = transactionFactory;
    }

    private Account isIdAvailable(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account doesn't exist!"));
    }

    private void logTransaction(Long accountId, double amount, TransactionType transactionType) {
        Transaction transaction = transactionFactory.createTransaction(accountId, amount, transactionType);
        transactionRepository.save(transaction);
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    /**
     * @param id
     * @return AccountDto
     */
    @Override
    public AccountDto getAccountById(Long id) {
        Account account = isIdAvailable(id);
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        Account account = isIdAvailable(id);
        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        //Logging the transaction into database
        logTransaction(id, amount, TransactionType.DEPOSIT);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    /**
     * @param id
     * @param amount
     * @return AccountDto object
     */
    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account = isIdAvailable(id);
        if(account.getBalance() < amount)
            throw new RuntimeException("Insufficient Balance");
        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        //Logging the withdrawal of transaction into database
        logTransaction(id, amount, TransactionType.WITHDRAW);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    /**
     * @return
     */
    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map((AccountMapper::mapToAccountDto))
                .collect(Collectors.toList());
    }

    /**
     * @param id
     */
    @Override
    public void deleteAccount(Long id) {
        isIdAvailable(id);
        accountRepository.deleteById(id);
    }

    /**
     * @param transferFundDto
     */
    @Override
    @Transactional
    public void transferFunds(TransferFundDto transferFundDto) {
        Account fromAccount = isIdAvailable(transferFundDto.fromAccountId());
        Account toAccount = isIdAvailable(transferFundDto.toAccountId());

        //Debiting
        double amount = transferFundDto.amount();
        double newFromAccountBalance = fromAccount.getBalance() - amount;

        if (newFromAccountBalance < 0) {
            throw new RuntimeException("Insufficient balance in account: " + fromAccount.getId());
        }
        fromAccount.setBalance(newFromAccountBalance);

        //Crediting
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        logTransaction(transferFundDto.fromAccountId(),
                amount, TransactionType.TRANSFER);
    }

    /**
     * @param accountId
     * @return
     */
    @Override
    public List<TransactionDto> getAccountTransactions(Long accountId) {
        isIdAvailable(accountId);
        List<Transaction> transactions = transactionRepository
                .findByAccountIdOrderByTimestampDesc(accountId);

        return transactions.stream()
                .map((TransactionMapper::convertEntityToDto))
                .collect(Collectors.toList());
    }

}
