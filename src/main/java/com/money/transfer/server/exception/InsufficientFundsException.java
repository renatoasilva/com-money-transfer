package com.money.transfer.server.exception;

public class InsufficientFundsException extends RuntimeException {

	private static final long serialVersionUID = -4926890727903249758L;
	private static final String DEFAULT_MESSAGE = "Account %s has insufficient funds to complete the transfer.";

	public InsufficientFundsException(String accountId) {
		super(String.format(DEFAULT_MESSAGE, accountId));
	}
}
