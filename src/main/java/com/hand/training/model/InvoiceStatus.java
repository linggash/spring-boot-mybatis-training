package com.hand.training.model;

public enum InvoiceStatus {
    SUCCESS("S"),
    DRAFT("D"),
    FAIL("F"),
    CANCELLED("C");

    public final String label;

    private InvoiceStatus(String label){
        this.label = label;
    }
}
