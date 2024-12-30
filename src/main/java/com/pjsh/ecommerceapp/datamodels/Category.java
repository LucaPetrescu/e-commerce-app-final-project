package com.pjsh.ecommerceapp.datamodels;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Category extends BaseEntity {

    private String name;
    private String description;

    @ElementCollection
    @OneToMany(targetEntity=Product.class, mappedBy="category", cascade= CascadeType.ALL)
    private List<Product> products;

}
