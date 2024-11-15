package com.hand.training.service;

import com.hand.training.model.*;

import java.util.List;

public interface InvoiceService {

    List<InvoiceHeaderResponse> list(int page, int size, InvoiceStatus status, String type);

    InvoiceHeaderResponse detail(Long id);

    void remove(List<Long> ids);

    List<InvoiceHeaderResponse> save(List<InvoiceHeaderRequest> requests);

    List<InvoiceHeaderResponse> saveData(List<InvoiceHeaderRequest> requests);

    List<InvoiceLineResponse> saveDataLine(Long id, List<InvoiceLineRequest> requests);
}
