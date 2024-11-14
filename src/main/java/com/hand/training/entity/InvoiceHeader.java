package com.hand.training.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceHeader {

    private Long invoiceHeaderId;

    private String invoiceNumber;

    private String status;

    private String invoiceType;

    private Double totalAmount;

    private Long delFlag;

    private String remark;

    private Date creationDate;

    private Long createdBy;

    private Long lastUpdatedBy;

    private Date lastUpdateDate;

    private List<InvoiceLine> invoiceLines;
}
