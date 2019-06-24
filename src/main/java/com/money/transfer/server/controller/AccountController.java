package com.money.transfer.server.controller;

import javax.inject.Inject;

import com.money.transfer.server.model.Amount;
import com.money.transfer.server.service.AccountService;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;

@Controller("/money-transfer/accounts")
@Validated
public class AccountController {

	@Inject
	private AccountService accountService;

	@Post
	public String createAccount() {
		return accountService.create();
	}

	@Get(uri = "/{accountId}")
	@Error(status = HttpStatus.BAD_REQUEST)
	public Amount getAccountBalance(String accountId) {
		return accountService.getAccountBalance(accountId);
	}

}
