package com.miguelnarei.functionalprogramming.controller;

import com.miguelnarei.functionalprogramming.declarative.Purchase;

public class PurchaseMapper {

    public static PurchaseResonseDto fromPurchase(Purchase purchase) {
        return PurchaseResonseDto.builder()
            .pan(purchase.getCard().getPan())
            .cardType(purchase.getCard().getCardType().name())
            .nif(purchase.getNif())
            .amount(purchase.getAmount())
            .personId(purchase.getPersonId())
            .status(purchase.getStatus())
            .fee(purchase.getFee())
            .build();
    }
}
