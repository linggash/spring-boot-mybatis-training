package com.hand.training.mapper;

import com.hand.training.entity.InvoiceHeader;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InvoiceHeaderMapper {

    void create(InvoiceHeader build);

    List<InvoiceHeader> list (
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("status")String status,
            @Param("type") String type
    );

    InvoiceHeader detail(Long id);

    void remove(Long id);

    void update(InvoiceHeader invoiceHeader);
}
