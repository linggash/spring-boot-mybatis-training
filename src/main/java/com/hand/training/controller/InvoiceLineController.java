package com.hand.training.controller;

import com.hand.training.model.*;
import com.hand.training.service.InvoiceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceLineController {

    @Autowired
    private InvoiceService invoiceService;

    @ApiOperation("saveData")
    @PostMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<InvoiceLineResponse>> saveData(
            @PathVariable Long id,
            @RequestBody List<InvoiceLineRequest> requests
    ) {
        List<InvoiceLineResponse> response = invoiceService.saveDataLine(id, requests);
        return WebResponse.<List<InvoiceLineResponse>>builder()
                .status("success")
                .data(response)
                .build();
    }
}
