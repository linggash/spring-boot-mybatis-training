package com.hand.training.service.impl;

import com.hand.training.entity.InvoiceHeader;
import com.hand.training.entity.InvoiceLine;
import com.hand.training.mapper.InvoiceHeaderMapper;
import com.hand.training.mapper.InvoiceLineMapper;
import com.hand.training.model.*;
import com.hand.training.service.InvoiceService;
import com.hand.training.service.ValidationService;
import jakarta.validation.ConstraintViolationException;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceHeaderMapper invoiceHeaderMapper;

    @Autowired
    private InvoiceLineMapper invoiceLineMapper;

    @Autowired
    private ValidationService validationService;

    @Override
    public List<InvoiceHeaderResponse> list(
            int page,
            int size,
            InvoiceStatus status,
            String type
    ) {
        int offset = (page - 1) * size;
        String invoiceStatus = "";
        if (status != null) {
            invoiceStatus = status.value;
        }
        List<InvoiceHeader> invoiceHeaders = invoiceHeaderMapper.list(size, offset, invoiceStatus, type);
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
                        invoiceLine.getTotalAmount(),
                        invoiceLine.getRemark()
                ));
            });
            responses.add(new InvoiceHeaderResponse(
                    invoiceHeader.getInvoiceHeaderId(),
                    invoiceHeader.getInvoiceNumber(),
                    invoiceHeader.getStatus(),
                    invoiceHeader.getInvoiceType(),
                    invoiceHeader.getTotalAmount(),
                    invoiceHeader.getRemark(),
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
                        invoiceLine.getTotalAmount(),
                        invoiceLine.getRemark()
                ));
            });
            return new InvoiceHeaderResponse(
                    invoiceHeader.getInvoiceHeaderId(),
                    invoiceHeader.getInvoiceNumber(),
                    invoiceHeader.getStatus(),
                    invoiceHeader.getInvoiceType(),
                    invoiceHeader.getTotalAmount(),
                    invoiceHeader.getRemark(),
                    invoiceLines
            );
        }
    }

    @Override
    @Transactional
    public void remove(List<Long> ids) {
        ids.forEach(id -> {
            invoiceHeaderMapper.remove(id);
            invoiceLineMapper.remove(id);
        });
    }

    @Override
    @Transactional
    public List<InvoiceHeaderResponse> save(List<InvoiceHeaderRequest> requests) {

        requests.forEach(invoiceHeaderRequest -> {
            InvoiceHeader invoiceHeader = invoiceHeaderMapper.detail(invoiceHeaderRequest.getInvoiceHeaderId());
            if (invoiceHeader == null) {
                invoiceHeaderMapper.create(InvoiceHeader.builder()
                                .invoiceHeaderId(invoiceHeaderRequest.getInvoiceHeaderId())
                                .invoiceNumber(UUID.randomUUID().toString())
                                .status(invoiceHeaderRequest.getStatus())
                                .invoiceType(invoiceHeaderRequest.getInvoiceType())
                                .remark(invoiceHeaderRequest.getRemark())
                        .build());
                invoiceHeaderRequest.getInvoiceLines().forEach(invoiceLineRequest -> {
                    invoiceLineMapper.create(InvoiceLine.builder()
                            .invoiceLineId(invoiceLineRequest.getInvoiceLineId())
                            .invoiceHeaderId(invoiceHeaderRequest.getInvoiceHeaderId())
                            .itemNumber(invoiceLineRequest.getItemNumber())
                            .itemDescription(invoiceLineRequest.getItemDescription())
                            .unitPrice(invoiceLineRequest.getUnitPrice())
                            .quantity(invoiceLineRequest.getQuantity())
                            .totalAmount(invoiceLineRequest.getUnitPrice() * invoiceLineRequest.getQuantity() )
                            .remark(invoiceLineRequest.getRemark())
                            .build());
                });
            } else {
                invoiceHeader.setInvoiceNumber(invoiceHeaderRequest.getInvoiceNumber());
                invoiceHeader.setStatus(invoiceHeaderRequest.getStatus());
                invoiceHeader.setInvoiceType(invoiceHeaderRequest.getInvoiceType());
                invoiceHeader.setRemark(invoiceHeaderRequest.getRemark());
                invoiceHeaderMapper.update(invoiceHeader);
                invoiceHeaderRequest.getInvoiceLines().forEach(invoiceLineRequest -> {
                    InvoiceLine invoiceLine = invoiceLineMapper.get(invoiceLineRequest.getInvoiceLineId());
                    if (invoiceLine == null) {
                        invoiceLineMapper.create(InvoiceLine.builder()
                                        .invoiceLineId(invoiceLineRequest.getInvoiceLineId())
                                        .invoiceHeaderId(invoiceHeader.getInvoiceHeaderId())
                                        .itemNumber(invoiceLineRequest.getItemNumber())
                                        .itemDescription(invoiceLineRequest.getItemDescription())
                                        .unitPrice(invoiceLineRequest.getUnitPrice())
                                        .quantity(invoiceLineRequest.getQuantity())
                                        .totalAmount(invoiceLineRequest.getUnitPrice() * invoiceLineRequest.getQuantity() )
                                        .remark(invoiceLineRequest.getRemark())
                                .build());
                    } else {
                        invoiceLine.setItemNumber(invoiceLineRequest.getItemNumber());
                        invoiceLine.setItemDescription(invoiceLineRequest.getItemDescription());
                        invoiceLine.setUnitPrice(invoiceLineRequest.getUnitPrice());
                        invoiceLine.setQuantity(invoiceLineRequest.getQuantity());
                        invoiceLine.setTotalAmount(invoiceLineRequest.getUnitPrice() * invoiceLineRequest.getQuantity());
                        invoiceLine.setRemark(invoiceLineRequest.getRemark());
                        invoiceLineMapper.update(invoiceLine);
                    }
                });
            }
        });
        List<InvoiceHeaderResponse> responses = new ArrayList<>();
        requests.forEach(invoiceHeaderRequest -> {
            InvoiceHeader invoiceHeader = invoiceHeaderMapper.detail(invoiceHeaderRequest.getInvoiceHeaderId());
            List<InvoiceLineResponse> invoiceLines = new ArrayList<>();
            invoiceHeader.getInvoiceLines().forEach(invoiceLine -> {
                invoiceLines.add(new InvoiceLineResponse(
                        invoiceLine.getInvoiceLineId(),
                        invoiceLine.getItemNumber(),
                        invoiceLine.getItemDescription(),
                        invoiceLine.getUnitPrice(),
                        invoiceLine.getQuantity(),
                        invoiceLine.getTotalAmount(),
                        invoiceLine.getRemark()
                ));

            });
            responses.add(new InvoiceHeaderResponse(
                    invoiceHeader.getInvoiceHeaderId(),
                    invoiceHeader.getInvoiceNumber(),
                    invoiceHeader.getStatus(),
                    invoiceHeader.getInvoiceType(),
                    invoiceHeader.getTotalAmount(),
                    invoiceHeader.getRemark(),
                    invoiceLines
            ));
        });
        return responses;
    }

    @Override
    public List<InvoiceHeaderResponse> saveData(List<InvoiceHeaderRequest> requests) {
        requests.forEach(invoiceHeaderRequest -> {
            validationService.validate(invoiceHeaderRequest);
            InvoiceHeader invoiceHeader = invoiceHeaderMapper.detail(invoiceHeaderRequest.getInvoiceHeaderId());
            if (invoiceHeader == null) {
                invoiceHeaderMapper.create(InvoiceHeader.builder()
                        .invoiceHeaderId(invoiceHeaderRequest.getInvoiceHeaderId())
                        .invoiceNumber(UUID.randomUUID().toString().substring(30))
                        .status(invoiceHeaderRequest.getStatus())
                        .invoiceType(invoiceHeaderRequest.getInvoiceType())
                        .remark(invoiceHeaderRequest.getRemark())
                        .build());
                invoiceHeaderRequest.getInvoiceLines().forEach(invoiceLineRequest -> {
                    invoiceLineMapper.create(InvoiceLine.builder()
                            .invoiceLineId(invoiceLineRequest.getInvoiceLineId())
                            .invoiceHeaderId(invoiceHeaderRequest.getInvoiceHeaderId())
                            .itemNumber(invoiceLineRequest.getItemNumber())
                            .itemDescription(invoiceLineRequest.getItemDescription())
                            .unitPrice(invoiceLineRequest.getUnitPrice())
                            .quantity(invoiceLineRequest.getQuantity())
                            .totalAmount(invoiceLineRequest.getUnitPrice() * invoiceLineRequest.getQuantity() )
                            .remark(invoiceLineRequest.getRemark())
                            .build());
                });
            } else {
                invoiceHeader.setStatus(invoiceHeaderRequest.getStatus());
                invoiceHeader.setInvoiceType(invoiceHeaderRequest.getInvoiceType());
                invoiceHeader.setRemark(invoiceHeaderRequest.getRemark());
                invoiceHeaderMapper.update(invoiceHeader);
                invoiceHeaderRequest.getInvoiceLines().forEach(invoiceLineRequest -> {
                    InvoiceLine invoiceLine = invoiceLineMapper.get(invoiceLineRequest.getInvoiceLineId());
                    if (invoiceLine == null) {
                        invoiceLineMapper.create(InvoiceLine.builder()
                                .invoiceLineId(invoiceLineRequest.getInvoiceLineId())
                                .invoiceHeaderId(invoiceHeader.getInvoiceHeaderId())
                                .itemNumber(invoiceLineRequest.getItemNumber())
                                .itemDescription(invoiceLineRequest.getItemDescription())
                                .unitPrice(invoiceLineRequest.getUnitPrice())
                                .quantity(invoiceLineRequest.getQuantity())
                                .totalAmount(invoiceLineRequest.getUnitPrice() * invoiceLineRequest.getQuantity() )
                                .remark(invoiceLineRequest.getRemark())
                                .build());
                    } else {
                        invoiceLine.setItemNumber(invoiceLineRequest.getItemNumber());
                        invoiceLine.setItemDescription(invoiceLineRequest.getItemDescription());
                        invoiceLine.setUnitPrice(invoiceLineRequest.getUnitPrice());
                        invoiceLine.setQuantity(invoiceLineRequest.getQuantity());
                        invoiceLine.setTotalAmount(invoiceLineRequest.getUnitPrice() * invoiceLineRequest.getQuantity());
                        invoiceLine.setRemark(invoiceLineRequest.getRemark());
                        invoiceLineMapper.update(invoiceLine);
                    }
                });
            }
        });
        List<InvoiceHeaderResponse> responses = new ArrayList<>();
        requests.forEach(invoiceHeaderRequest -> {
            InvoiceHeader invoiceHeader = invoiceHeaderMapper.detail(invoiceHeaderRequest.getInvoiceHeaderId());
            List<InvoiceLineResponse> invoiceLines = new ArrayList<>();
            invoiceHeader.getInvoiceLines().forEach(invoiceLine -> {
                invoiceLines.add(new InvoiceLineResponse(
                        invoiceLine.getInvoiceLineId(),
                        invoiceLine.getItemNumber(),
                        invoiceLine.getItemDescription(),
                        invoiceLine.getUnitPrice(),
                        invoiceLine.getQuantity(),
                        invoiceLine.getTotalAmount(),
                        invoiceLine.getRemark()
                ));

            });
            responses.add(new InvoiceHeaderResponse(
                    invoiceHeader.getInvoiceHeaderId(),
                    invoiceHeader.getInvoiceNumber(),
                    invoiceHeader.getStatus(),
                    invoiceHeader.getInvoiceType(),
                    invoiceHeader.getTotalAmount(),
                    invoiceHeader.getRemark(),
                    invoiceLines
            ));
        });
        return responses;
    }

    @Override
    public List<InvoiceLineResponse> saveDataLine(Long id, List<InvoiceLineRequest> requests) {
        validationService.validate(requests);
        InvoiceHeader invoiceHeader = invoiceHeaderMapper.detail(id);
        if (invoiceHeader == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Not Found");
        }
        List<InvoiceLineResponse> responses = new ArrayList<>();
        requests.forEach(invoiceLineRequest -> {
            InvoiceLine invoiceLine = invoiceLineMapper.get(invoiceLineRequest.getInvoiceLineId());
            if (invoiceLine == null) {
                invoiceLineMapper.create(InvoiceLine.builder()
                        .invoiceLineId(invoiceLineRequest.getInvoiceLineId())
                        .invoiceHeaderId(id)
                        .itemNumber(invoiceLineRequest.getItemNumber())
                        .itemDescription(invoiceLineRequest.getItemDescription())
                        .unitPrice(invoiceLineRequest.getUnitPrice())
                        .quantity(invoiceLineRequest.getQuantity())
                        .totalAmount(invoiceLineRequest.getUnitPrice() * invoiceLineRequest.getQuantity() )
                        .remark(invoiceLineRequest.getRemark())
                        .build());
            } else {
                invoiceLine.setItemNumber(invoiceLineRequest.getItemNumber());
                invoiceLine.setItemDescription(invoiceLineRequest.getItemDescription());
                invoiceLine.setUnitPrice(invoiceLineRequest.getUnitPrice());
                invoiceLine.setQuantity(invoiceLineRequest.getQuantity());
                invoiceLine.setTotalAmount(invoiceLineRequest.getUnitPrice() * invoiceLineRequest.getQuantity());
                invoiceLine.setRemark(invoiceLineRequest.getRemark());
                invoiceLineMapper.update(invoiceLine);
            }
        });
        requests.forEach(invoiceLine -> {
            InvoiceLine newInvoiceLine = invoiceLineMapper.get(invoiceLine.getInvoiceLineId());
            responses.add(new InvoiceLineResponse(
                    newInvoiceLine.getInvoiceLineId(),
                    newInvoiceLine.getItemNumber(),
                    newInvoiceLine.getItemDescription(),
                    newInvoiceLine.getUnitPrice(),
                    newInvoiceLine.getQuantity(),
                    newInvoiceLine.getTotalAmount(),
                    newInvoiceLine.getRemark()
            ));
        });
        return responses;
    }
}
