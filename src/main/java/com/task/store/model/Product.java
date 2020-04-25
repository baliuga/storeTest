package com.task.store.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@ApiModel(description = "Product model")
@Entity
@Table(name = "PRODUCT")
public class Product {

    @ApiModelProperty(notes = "The database generated product id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(notes = "Product name")
    @NotBlank(message = "Please provide name")
    @Column(name = "NAME")
    private String name;

    @ApiModelProperty(notes = "Product price")
    @NotNull(message = "Please provide price")
    @Column(name = "PRICE")
    private BigDecimal price;

    @ApiModelProperty(notes = "An 8-digits EAN product code")
    @Pattern(regexp = "[\\d]{8}", message = "Please provide 8-digits EAN")
    @NotBlank(message = "Please provide EAN code")
    @Column(name = "EAN", unique = true)
    private String ean;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }
}
