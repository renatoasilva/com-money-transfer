package com.money.transfer.server.exception;

public class AccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1069261338954993431L;
	private static final String DEFAULT_MESSAGE = "'%s' is an invalid account. Please provide a valid account.";

	public AccountNotFoundException(String accountId) {
		super(String.format(DEFAULT_MESSAGE, accountId));
	}

	public AccountNotFoundException(Throwable exception) {
		super(DEFAULT_MESSAGE, exception);
	}

}
