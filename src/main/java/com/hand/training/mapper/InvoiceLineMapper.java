package com.hand.training.mapper;

import com.hand.training.entity.InvoiceLine;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvoiceLineMapper {

    void create(InvoiceLine build);

    InvoiceLine get(Long id);

    void update(InvoiceLine invoiceLine);

    void remove(Long id);

}
