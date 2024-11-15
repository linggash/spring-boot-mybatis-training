package com.hand.training.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceLineResponse {

    private Long invoiceLineId;

    private String itemNumber;

    private String itemDescription;

    private Double unitPrice;

    private Double quantity;

    private Double totalAmount;

    private String remark;
}
