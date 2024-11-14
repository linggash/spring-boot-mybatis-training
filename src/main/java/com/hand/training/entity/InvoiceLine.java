package com.hand.training.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceLine {

    private Long invoiceLineId;

    private Long invoiceHeaderId;

    private String itemNumber;

    private String itemDescription;

    private Double unitPrice;;

    private Integer quantity;

    private Double totalAmount;

    private String remark;

    private Date creationDate;

    private String createdBy;

    private String lastUpdatedBy;

    private Date lastUpdateDate;
}
