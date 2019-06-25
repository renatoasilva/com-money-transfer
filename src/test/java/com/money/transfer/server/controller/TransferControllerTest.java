package com.money.transfer.server.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.money.transfer.server.exception.AccountNotFoundException;
import com.money.transfer.server.exception.InsufficientFundsException;
import com.money.transfer.server.model.Transaction;
import com.money.transfer.server.model.TransferRequest;
import com.money.transfer.server.service.TransferService;
import com.money.transfer.server.service.TransferServiceImpl;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;

@MicronautTest
@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {
	private static final String ACCOUNT_ID = "accountId";
	private static final String ACCOUNT_ID2 = "accountId2";
	private static final String API_PATH = "/money-transfer/transfers";

	@Inject
	private TransferService transferService;

	@Inject
	@Client("/")
	private HttpClient client;

	@Inject
	private ObjectMapper objectMapper;

	private List<Transaction> listTransfers = ImmutableList.of(Transaction.builder().build());

	@Test
	public void testGetTransfersByAccount_Success() throws Exception {
		when(transferService.getTransfers(ACCOUNT_ID)).thenReturn(listTransfers);

		@SuppressWarnings("rawtypes")
		final List responseBody = client.toBlocking().retrieve(HttpRequest.GET(API_PATH + "/accounts/" + ACCOUNT_ID), List.class);
		List<Transaction> actualList = objectMapper.readValue(objectMapper.writeValueAsString(responseBody), new TypeReference<List<Transaction>>() {}) ;
		assertThat(actualList, is(listTransfers));
	}

	@Test
	public void testGetTransfersByAccount_ZeroTransfers_Success() throws Exception {
		when(transferService.getTransfers(ACCOUNT_ID)).thenReturn(Collections.emptyList());

		@SuppressWarnings("unchecked")
		final List<Transaction> actual = client.toBlocking().retrieve(HttpRequest.GET(API_PATH + "/accounts/" + ACCOUNT_ID), List.class);
		assertThat(actual, is(Collections.emptyList()));
	}

	@Test
	public void testCreateTransferRequest_Success() throws Exception {
		when(transferService.createTransfer(any(TransferRequest.class))).thenReturn(listTransfers.get(0));
		TransferRequest request = TransferRequest.builder()
				.originAccountId(ACCOUNT_ID)
				.recipientAccountId(ACCOUNT_ID2)
				.amount(BigDecimal.TEN)
				.build();

		final Transaction actual = client.toBlocking().retrieve(HttpRequest.POST(API_PATH, request), Transaction.class);
		assertThat(actual, is(listTransfers.get(0)));
		verify(transferService).createTransfer(request);
	}

	@Test
	public void testCreateTransferRequest_InsufficientFundsException_Error() throws Exception {
		when(transferService.createTransfer(any(TransferRequest.class))).thenThrow(new InsufficientFundsException(ACCOUNT_ID));
		TransferRequest request = TransferRequest.builder()
				.originAccountId(ACCOUNT_ID)
				.recipientAccountId(ACCOUNT_ID2)
				.amount(BigDecimal.TEN)
				.build();

		Assertions.assertThrows(HttpClientResponseException.class, () -> client.toBlocking()
				.retrieve(HttpRequest.POST(API_PATH, request), Transaction.class));
	}

	@Test
	public void testCreateTransferRequest_NegativeAmount_Error() throws Exception {
		TransferRequest request = TransferRequest.builder()
				.originAccountId(ACCOUNT_ID)
				.recipientAccountId(ACCOUNT_ID2)
				.amount(BigDecimal.valueOf(-1))
				.build();

		Assertions.assertThrows(HttpClientResponseException.class, () -> client.toBlocking()
				.retrieve(HttpRequest.POST(API_PATH, request), Transaction.class));
	}

	@MockBean(TransferServiceImpl.class)
	TransferService transferService() {
		return mock(TransferService.class);
	}

	@Test
	public void testTopUp_Success() throws Exception {
		when(transferService.topUp(any(String.class), any(BigDecimal.class))).thenReturn(listTransfers.get(0));

		final Transaction actual = client.toBlocking().retrieve(HttpRequest.POST(String.format(API_PATH + "/accounts/%s/amount/%s", ACCOUNT_ID, 10), ""), Transaction.class);
		assertThat(actual, is(listTransfers.get(0)));
		verify(transferService).topUp(ACCOUNT_ID, BigDecimal.TEN);
	}

	@Test
	public void testTopUp_ValidationError() throws Exception {
		when(transferService.topUp(any(String.class), any(BigDecimal.class))).thenThrow(new AccountNotFoundException(ACCOUNT_ID));

		Assertions.assertThrows(HttpClientResponseException.class, () -> client.toBlocking()
				.retrieve(HttpRequest.POST(String.format(API_PATH + "/accounts/%s/amount/%s", ACCOUNT_ID, 10), ""), Transaction.class));
	}

	@Test
	public void testTopUp_ZeroAmountError() throws Exception {
		Assertions.assertThrows(HttpClientResponseException.class, () -> client.toBlocking()
				.retrieve(HttpRequest.POST(String.format(API_PATH + "/accounts/%s/amount/%s", ACCOUNT_ID, 0), ""), Transaction.class));
		verifyNoMoreInteractions(transferService);
	}

}
