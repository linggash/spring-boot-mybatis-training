package com.hand.training.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceHeaderResponse {

    private Long invoiceHeaderId;

    private String invoiceNumber;

    private String status;

    private String invoiceType;

    private Double totalAmount;

    private String remark;

    private List<InvoiceLineResponse> invoiceLines;
}
