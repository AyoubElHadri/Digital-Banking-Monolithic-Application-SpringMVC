package com.example.digitalbank.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
@DiscriminatorValue(value = "CA")
@AllArgsConstructor
@NoArgsConstructor
public class CurrentAccount extends BankAccount{
    private double overDraft;
}
