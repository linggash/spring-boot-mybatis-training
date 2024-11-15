package com.hand.training.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class InvoiceHeaderRequest {

    private Long invoiceHeaderId;

    private String invoiceNumber;

    @NotBlank(message = "Status must be filled")
    @Size(max = 1)
    @Pattern(regexp = "[DFSC]", message = "Input must be one of the following characters: D, F, S, or C")
    private String status;

    @NotBlank(message = "invoiceType must be filled")
    private String invoiceType;

    private String remark;

    private List<InvoiceLineRequest> invoiceLines;
}
