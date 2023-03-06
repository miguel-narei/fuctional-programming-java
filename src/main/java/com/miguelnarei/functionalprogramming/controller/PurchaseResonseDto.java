package com.miguelnarei.functionalprogramming.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@With
public class PurchaseResonseDto {

    String pan;
    String cardType;
    String nif;
    double amount;
    Double fee;
    String personId;
    String status;
}
