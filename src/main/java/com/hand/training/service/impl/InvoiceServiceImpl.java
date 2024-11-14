package com.hand.training.service.impl;

import com.hand.training.entity.InvoiceHeader;
import com.hand.training.mapper.InvoiceHeaderMapper;
import com.hand.training.model.InvoiceHeaderResponse;
import com.hand.training.model.InvoiceLineResponse;
import com.hand.training.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceHeaderMapper invoiceHeaderMapper;

    @Override
    public List<InvoiceHeaderResponse> list(int page, int size) {
        int offset = (page - 1) * size;
        List<InvoiceHeader> invoiceHeaders = invoiceHeaderMapper.list(size, offset);
        List<InvoiceHeaderResponse> responses = new ArrayList<>();
        invoiceHeaders.forEach(invoiceHeader -> {
            List<InvoiceLineResponse> invoiceLines = new ArrayList<>();
            invoiceHeader.getInvoiceLines().forEach(invoiceLine -> {
                invoiceLines.add(new InvoiceLineResponse(
                        invoiceLine.getInvoiceLineId(),
                        invoiceLine.getItemNumber(),
                        invoiceLine.getItemDescription(),
                        invoiceLine.getUnitPrice(),
                        invoiceLine.getQuantity(),
                        invoiceLine.getTotalAmount()
                ));
            });
            responses.add(new InvoiceHeaderResponse(
                    invoiceHeader.getInvoiceHeaderId(),
                    invoiceHeader.getInvoiceNumber(),
                    invoiceHeader.getStatus(),
                    invoiceHeader.getInvoiceType(),
                    invoiceHeader.getTotalAmount(),
                    invoiceLines
            ));
        });
        return responses;
    }
}
