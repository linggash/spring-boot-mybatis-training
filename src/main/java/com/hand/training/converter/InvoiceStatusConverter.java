package com.hand.training.converter;

import com.hand.training.model.InvoiceStatus;
import org.springframework.core.convert.converter.Converter;

public class InvoiceStatusConverter implements Converter<String, InvoiceStatus> {
    @Override
    public InvoiceStatus convert(String source) {
        switch (source) {
            case "S" :
                return InvoiceStatus.SUCCESS;
            case "F" :
                return InvoiceStatus.FAIL;
            case "D" :
                return InvoiceStatus.DRAFT;
            case "C" :
                return InvoiceStatus.CANCELLED;
            default: return null;
        }
    }
}
