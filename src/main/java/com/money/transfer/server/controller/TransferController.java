package com.money.transfer.server.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.money.transfer.server.model.TransferRequest;
import com.money.transfer.server.model.Transaction;
import com.money.transfer.server.service.TransferService;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.validation.Validated;

@Controller("/money-transfer/transfers")
@Validated
public class TransferController {

	@Inject
	private TransferService transferService;

	@Get("/accounts/{accountId}")
	public List<Transaction> getTransfersByAccount(@NotBlank String accountId) {
		return transferService.getTransfers(accountId);
	}

	@Post
	public Transaction createTransferRequest(@Body @Valid TransferRequest inputRequest) {
		return transferService.createTransfer(inputRequest);
	}

	@Post("/accounts/{accountId}/amount/{amount}")
	public Transaction topUp(@NotBlank String accountId,@NotNull @Min(1) BigDecimal amount) {
		return transferService.topUp(accountId, amount);
	}

}
