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
public class User extends BaseEntity {

    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy="user", cascade= CascadeType.ALL)
    private List<Order> orders;

}
