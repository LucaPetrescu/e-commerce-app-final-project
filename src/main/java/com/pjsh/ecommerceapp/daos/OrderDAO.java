package com.pjsh.ecommerceapp.daos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDAO {
    private Integer userId;
    private List<Integer> productIds;
    private List<Integer> quantities;
}
