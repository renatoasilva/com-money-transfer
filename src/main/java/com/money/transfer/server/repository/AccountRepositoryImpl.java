package com.money.transfer.server.repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import com.money.transfer.server.exception.AccountNotFoundException;
import com.money.transfer.server.exception.InsufficientFundsException;
import com.money.transfer.server.model.Amount;
import com.money.transfer.server.util.Utils;

@Singleton
public class AccountRepositoryImpl implements AccountRepository {

	private Map<String, Amount> accounts = new ConcurrentHashMap<>();

	@Override
	public Amount getAccountBalance(String accountId) {
		if(!accounts.containsKey(accountId)) {
			throw new AccountNotFoundException(accountId);
		}
		return accounts.get(accountId);
	}

	@Override
	public String createAccount() {
		String newAccountId = Utils.generateUUID();

		while (accounts.containsKey(newAccountId)) {
			newAccountId = UUID.randomUUID().toString();
		}
		accounts.put(newAccountId, Amount.builder().units(BigDecimal.ZERO).build());
		return newAccountId;
	}

	@Override
	public void updateAccountBalance(String accountId, Amount transactionAmount, Operation operation) {
		Amount accountBalance = getAccountBalance(accountId);
		if (Operation.CREDIT.equals(operation)) {
			accountBalance.setUnits(accountBalance.getUnits().add(transactionAmount.getUnits()));
		} else if (Operation.DEBIT.equals(operation)) {
			BigDecimal newBalance = accountBalance.getUnits().subtract(transactionAmount.getUnits());
			if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
				throw new InsufficientFundsException(accountId);
			}
			accountBalance.setUnits(newBalance);
		}
	}

}
