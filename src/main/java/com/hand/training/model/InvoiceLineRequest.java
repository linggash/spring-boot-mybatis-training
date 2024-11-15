package com.hand.training.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceLineRequest {

    private Long invoiceLineId;

    private String itemNumber;

    private String itemDescription;

    @NotBlank(message = "unitPrice must not be blank")
    private Double unitPrice;

    @NotBlank(message = "quantity must not be blank")
    private Double quantity;

    private String remark;
}
