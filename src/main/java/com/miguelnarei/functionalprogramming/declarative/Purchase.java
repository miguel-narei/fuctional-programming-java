package com.miguelnarei.functionalprogramming.declarative;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder(toBuilder = true)
@With
public class Purchase {

    Card card;
    String nif;
    double amount;
    Double fee;
    String personId;
    String status;

    @Value
    @Builder(toBuilder = true)
    @With
    public static class Card {

        String pan;
        CardType cardType;
    }

    public enum CardType {
        CREDIT,
        DEBIT
    }

}
