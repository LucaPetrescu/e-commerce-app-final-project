package com.pjsh.ecommerceapp.datamodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

//    @ElementCollection
    @OneToMany(targetEntity=Product.class, mappedBy="category", cascade= CascadeType.ALL)
    @JsonManagedReference
    private List<Product> products;

}
