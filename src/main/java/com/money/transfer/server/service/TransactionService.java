package com.money.transfer.server.service;

import java.util.List;

import com.money.transfer.server.model.Amount;
import com.money.transfer.server.model.Error;
import com.money.transfer.server.model.Transaction;

public interface TransactionService {

	List<Transaction> getTransactions(String accountId);

	Transaction addTransaction(String originAccountId, String recipientAccountId, Amount amount, List<Error> error);

}
