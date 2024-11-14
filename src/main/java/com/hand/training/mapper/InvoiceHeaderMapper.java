package com.hand.training.mapper;

import com.hand.training.entity.InvoiceHeader;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InvoiceHeaderMapper {
    List<InvoiceHeader> list(@Param("limit") int limit, @Param("offset") int offset);

    InvoiceHeader detail(Long id);

    void remove(Long id);
}
