package com.hand.training.service;

import com.hand.training.model.InvoiceHeaderResponse;

import java.util.List;

public interface InvoiceService {

    List<InvoiceHeaderResponse> list(int page, int size);

    InvoiceHeaderResponse detail(Long id);
}
