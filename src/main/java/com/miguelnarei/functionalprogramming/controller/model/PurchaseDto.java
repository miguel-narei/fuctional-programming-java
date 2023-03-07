package com.miguelnarei.functionalprogramming.controller.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PurchaseDto {
    String pan;
    double amount;
}
