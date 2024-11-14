package com.hand.training.controller;

import com.hand.training.model.InvoiceHeaderResponse;
import com.hand.training.model.InvoiceStatus;
import com.hand.training.model.WebResponse;
import com.hand.training.service.InvoiceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("list")
    public WebResponse<List<InvoiceHeaderResponse>> list(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "", name ="status") InvoiceStatus status,
            @RequestParam(defaultValue = "", name="type") String type
            ) {
        List<InvoiceHeaderResponse> response = invoiceService.list(page, size, status, type);
        return WebResponse.<List<InvoiceHeaderResponse>>builder()
                .data(response)
                .status("success")
                .build();
    }

    @GetMapping("/{id}")
    @ApiOperation("detail")
    public WebResponse<InvoiceHeaderResponse> detail(
            @PathVariable Long id
    ) {
        InvoiceHeaderResponse response = invoiceService.detail(id);
        return WebResponse.<InvoiceHeaderResponse>builder()
                .data(response)
                .status("success")
                .build();
    }

    @ApiOperation("remove")
    @DeleteMapping
    public WebResponse<String> remove(
            @RequestBody List<Long> ids
    ) {
        invoiceService.remove(ids);
        return WebResponse.<String>builder()
                .data("Data deleted")
                .status("success")
                .build();
    }
}
