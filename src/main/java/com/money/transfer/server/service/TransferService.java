package com.money.transfer.server.service;

import java.math.BigDecimal;
import java.util.List;

import com.money.transfer.server.model.TransferRequest;
import com.money.transfer.server.model.Transaction;

public interface TransferService {

	/**
	 * Return list of transactions for this account 
	 * @return potential empty list of transactions
	 */
	List<Transaction> getTransfers(String accountId);

	Transaction createTransfer(TransferRequest inputRequest);

	Transaction topUp(String accountId, BigDecimal amount);

}
