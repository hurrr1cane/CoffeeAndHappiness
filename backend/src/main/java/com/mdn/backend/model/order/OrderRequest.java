package com.mdn.backend.model.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private Integer userId;
    private List<Integer> foodIds;
}
