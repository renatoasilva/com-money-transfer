package com.money.transfer.server.repository;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.money.transfer.server.exception.AccountNotFoundException;
import com.money.transfer.server.exception.InsufficientFundsException;
import com.money.transfer.server.model.Amount;
import com.money.transfer.server.model.Transaction;
import com.money.transfer.server.repository.AccountRepositoryImpl;
import com.money.transfer.server.repository.AccountRepository.Operation;

@ExtendWith(MockitoExtension.class)
public class AccountRepositoryImplTest {
	private static final String ACCOUNT_ID = "accountId";
	private static final String INVALID_ACCOUNT_ID = "invalidAccountId";

	@Spy
	private Map<String, Amount> accounts = new HashMap<>();

	@Mock
	private List<Transaction> transactions;
	@InjectMocks
	private AccountRepositoryImpl underTest;

	@BeforeEach
	private void setup() {
		accounts.put(ACCOUNT_ID, Amount.builder().units(BigDecimal.ONE).build());
	}

	@Test
	public void testGetAccountBalance_Success() throws Exception {
		Amount amount = Amount.builder().units(BigDecimal.ONE).build();
		assertThat(underTest.getAccountBalance(ACCOUNT_ID), is(amount));
	}

	@Test
	public void testGetAccountBalance_NotFound_Fails() throws Exception {
		Assertions.assertThrows(AccountNotFoundException.class, () -> underTest.getAccountBalance(INVALID_ACCOUNT_ID));
	}

	@Test
	public void testCreateAccount_Success() throws Exception {
		assertThat(underTest.createAccount(), not(is(emptyString())));
	}

	@Test
	public void testUpdateAccountBalance_Credit_Success() throws Exception {
		Amount transactionAmount = Amount.builder().units(BigDecimal.ONE).build();

		underTest.updateAccountBalance(ACCOUNT_ID, transactionAmount, Operation.CREDIT);
		assertThat(accounts.get(ACCOUNT_ID).getUnits(), is(new BigDecimal(2L)));
	}

	@Test
	public void testUpdateAccountBalance_Debit_Success() throws Exception {
		Amount transactionAmount = Amount.builder().units(BigDecimal.ONE).build();

		underTest.updateAccountBalance(ACCOUNT_ID, transactionAmount, Operation.DEBIT);
		assertThat(accounts.get(ACCOUNT_ID).getUnits(), is(new BigDecimal(0L)));
	}

	@Test
	public void testUpdateAccountBalance_Debit_NotEnoughFunds_Fails() throws Exception {
		Amount transactionAmount = Amount.builder().units(BigDecimal.TEN).build();

		Assertions.assertThrows(InsufficientFundsException.class, () -> underTest.updateAccountBalance(ACCOUNT_ID, transactionAmount, Operation.DEBIT));
	}

}
