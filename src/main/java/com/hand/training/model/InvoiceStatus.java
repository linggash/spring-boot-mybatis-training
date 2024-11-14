package com.hand.training.model;

public enum InvoiceStatus {
    SUCCESS("S"),
    DRAFT("D"),
    FAIL("F"),
    CANCELLED("C");

    public final String value;

    private InvoiceStatus(String value){
        this.value = value;
    }
}
