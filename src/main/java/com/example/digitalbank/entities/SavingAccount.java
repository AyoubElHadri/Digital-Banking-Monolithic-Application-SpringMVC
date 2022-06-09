package com.example.digitalbank.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
@DiscriminatorValue(value = "SA")
@AllArgsConstructor
@NoArgsConstructor
public class SavingAccount extends BankAccount {
    private double interestRate;
}
