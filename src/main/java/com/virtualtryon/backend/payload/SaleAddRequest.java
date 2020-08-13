package com.virtualtryon.backend.payload;

import java.util.List;

public class SaleAddRequest {
    private List<SaleRequest> salesList;

    public List<SaleRequest> getSalesList() {
        return salesList;
    }
}
