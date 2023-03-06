package com.miguelnarei.functionalprogramming.controller;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PurchaseDto {
    String pan;
    double amount;
}
