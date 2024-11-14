package com.hand.training.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvoiceLineMapper {

    void remove(Long id);
}
