package com.money.transfer.server.service;

import com.money.transfer.server.model.Amount;
import com.money.transfer.server.repository.AccountRepository.Operation;

public interface AccountService {

	String create();

	Amount getAccountBalance(String accountId);

	void updateAccountBalance(String accountId, Amount transactionAmount, Operation operation);


}
