package com.example.digitalbank.dtos;

import com.example.digitalbank.enumerations.OperationType;
import lombok.Data;

import java.util.Date;
@Data
public class AccountOperationDto {
    private Long id;
    private Date date;
    private double amount;
    private OperationType type;
    private String description;
}
