package com.miguelnavarro.functionalprogramming.imperative;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Purchase {

    private Card card;
    private String nif;
    private double amount;
    private double fee;
    private String personId;
    private String status;

    @Data
    @Builder
    public static class Card {

        private String pan;
        private CardType cardType;
    }

    public enum CardType {
        CREDIT,
        DEBIT
    }

}
