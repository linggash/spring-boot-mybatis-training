package com.hand.training.service;

import com.hand.training.model.InvoiceHeaderResponse;
import com.hand.training.model.InvoiceStatus;

import java.util.List;

public interface InvoiceService {

    List<InvoiceHeaderResponse> list(int page, int size, InvoiceStatus status, String type);

    InvoiceHeaderResponse detail(Long id);

    void remove(List<Long> ids);
}
