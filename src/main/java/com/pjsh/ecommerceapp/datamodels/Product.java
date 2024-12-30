package com.pjsh.ecommerceapp.datamodels;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Product extends BaseEntity {

    private String name;
    private String description;
    private Integer availableQuantity;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

}
