package com.virtualtryon.backend.payload;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class SaleAddRequest {
    @NotNull
    @Size(min = 1)
    @Valid
    private List<SaleRequest> salesList;

    public List<SaleRequest> getSalesList() {
        return salesList;
    }

    public void setSalesList(List<SaleRequest> salesList) {
        this.salesList = salesList;
    }
}
