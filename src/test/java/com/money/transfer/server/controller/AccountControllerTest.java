package com.money.transfer.server.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.money.transfer.server.exception.AccountNotFoundException;
import com.money.transfer.server.model.Amount;
import com.money.transfer.server.service.AccountService;
import com.money.transfer.server.service.AccountServiceImpl;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;

@MicronautTest
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
	private static final String ACCOUNT_ID = "accountId";

	@Inject
	private AccountService accountService;

	@Inject
	@Client("/")
	private HttpClient client;

	@Test
	public void testCreateAccount_Success() throws Exception {
		when(accountService.create()).thenReturn("NEW_ACCOUNT_ID");

		final String actual = client.toBlocking().retrieve(HttpRequest.POST("/money-transfer/accounts", ""));
		assertThat(actual, is("NEW_ACCOUNT_ID"));
	}

	@Test
	public void testGetAccountBalance_Success() throws Exception {
		Amount amount = Amount.builder().units(BigDecimal.ONE).build();
		when(accountService.getAccountBalance(ACCOUNT_ID)).thenReturn(amount);

		final Amount actual = client.toBlocking().retrieve(HttpRequest.GET("/money-transfer/accounts/" + ACCOUNT_ID),
				Amount.class);
		assertThat(actual, is(amount));
		verify(accountService).getAccountBalance(ACCOUNT_ID);
	}

	@Test
	public void testGetAccountBalance_AccountNotFound_Fails() throws Exception {
		when(accountService.getAccountBalance(ACCOUNT_ID)).thenThrow(new AccountNotFoundException(ACCOUNT_ID));

		Assertions.assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(HttpRequest.GET("/money-transfer/accounts/" + ACCOUNT_ID),
				Amount.class));

		verify(accountService).getAccountBalance(ACCOUNT_ID);
	}

	@MockBean(AccountServiceImpl.class)
	AccountService accountService() {
		return mock(AccountService.class);
	}

}
