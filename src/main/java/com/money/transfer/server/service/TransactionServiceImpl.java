package com.money.transfer.server.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.money.transfer.server.model.Amount;
import com.money.transfer.server.model.Error;
import com.money.transfer.server.model.Transaction;
import com.money.transfer.server.repository.TransactionRepository;

@Singleton
public class TransactionServiceImpl implements TransactionService {

	@Inject
	private TransactionRepository transactionRepository;

	@Override
	public List<Transaction> getTransactions(String accountId) {
		return transactionRepository.getTransactions(accountId);
	}

	@Override
	public Transaction addTransaction(String originAccountId, String recipientAccountId, Amount amount,
			List<Error> errors) {
		return transactionRepository.addTransaction(originAccountId, recipientAccountId, amount, errors);
	}

}
