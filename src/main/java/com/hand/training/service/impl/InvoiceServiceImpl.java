package com.hand.training.service.impl;

import com.hand.training.entity.InvoiceHeader;
import com.hand.training.mapper.InvoiceHeaderMapper;
import com.hand.training.mapper.InvoiceLineMapper;
import com.hand.training.model.InvoiceHeaderResponse;
import com.hand.training.model.InvoiceLineResponse;
import com.hand.training.model.InvoiceStatus;
import com.hand.training.service.InvoiceService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceHeaderMapper invoiceHeaderMapper;

    @Autowired
    private InvoiceLineMapper invoiceLineMapper;

    @Override
    public List<InvoiceHeaderResponse> list(
            int page,
            int size,
            InvoiceStatus status,
            String type
    ) {
        int offset = (page - 1) * size;
        List<InvoiceHeader> invoiceHeaders = invoiceHeaderMapper.list(size, offset, status.value, type);
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

    @Override
    public InvoiceHeaderResponse detail(Long id) {
        InvoiceHeader invoiceHeader = invoiceHeaderMapper.detail(id);
        if (invoiceHeader == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Not Found");
        } else {
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
            return new InvoiceHeaderResponse(
                    invoiceHeader.getInvoiceHeaderId(),
                    invoiceHeader.getInvoiceNumber(),
                    invoiceHeader.getStatus(),
                    invoiceHeader.getInvoiceType(),
                    invoiceHeader.getTotalAmount(),
                    invoiceLines
            );
        }
    }

    @Override
    public void remove(List<Long> ids) {
        ids.forEach(id -> {
            invoiceHeaderMapper.remove(id);
            invoiceLineMapper.remove(id);
        });
    }
}
