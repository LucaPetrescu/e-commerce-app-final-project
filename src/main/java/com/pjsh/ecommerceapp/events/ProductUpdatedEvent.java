package com.pjsh.ecommerceapp.events;

//Class for defining an event

public class ProductUpdatedEvent {

    private Integer productId;
    private String updatedFields;

    public ProductUpdatedEvent(Integer productId, String updatedFields) {
        this.productId = productId;
        this.updatedFields = updatedFields;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getUpdatedFields() {
        return updatedFields;
    }

}
