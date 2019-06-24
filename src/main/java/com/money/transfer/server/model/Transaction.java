package com.money.transfer.server.model;

import java.time.LocalDateTime;

import com.money.transfer.server.util.Utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends AbstractErrorResponse{

	@Builder.Default
	private String id = Utils.generateUUID();
	@Builder.Default
	private LocalDateTime created = LocalDateTime.now();
	private Amount amount;
	private String originAccountId;
	private String recipientAccountId;
}
