package com.es.phoneshop.model.product;

import java.util.Objects;

public class Product {
    private Long id;
    private String code;
    private String description;
    /** null means there is no price because the product is outdated or new */
    private Price price;
    private int stock;
    private String imageUrl;

    public Product() {
    }

    public Product(Long id, String code, String description, Price price, int stock, String imageUrl) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public Product(Product product) {
        this.id = product.getId();
        this.code = product.getCode();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.imageUrl = product.getImageUrl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return stock == product.stock && Objects.equals(id, product.id) && Objects.equals(code, product.code) &&
                Objects.equals(description, product.description) && Objects.equals(price, product.price)  &&
                Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, description, price, stock, imageUrl);
    }
}