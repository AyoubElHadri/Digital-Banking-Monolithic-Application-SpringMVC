package com.example.digitalbank.dtos;

import lombok.Data;

@Data
public class CustomerDto {
    private Long accountID;
    private String name;
    private String email;

}
