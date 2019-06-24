package com.money.transfer.server.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.money.transfer.server.model.Amount;
import com.money.transfer.server.repository.AccountRepository;
import com.money.transfer.server.repository.AccountRepository.Operation;

@Singleton
public class AccountServiceImpl implements AccountService {

	@Inject
	private AccountRepository accountRepo;

	@Override
	public String create() {
		return accountRepo.createAccount();
	}

	@Override
	public Amount getAccountBalance(String accountId) {
		return accountRepo.getAccountBalance(accountId);
	}

	@Override
	public void updateAccountBalance(String accountId, Amount transactionAmount, Operation operation) {
		accountRepo.updateAccountBalance(accountId, transactionAmount, operation);
	}

}
