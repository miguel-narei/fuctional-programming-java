package com.miguelnavarro.functionalprogramming.imperative;

import com.miguelnavarro.functionalprogramming.imperative.Purchase.CardType;
import lombok.Value;

@Value
public class CardDetails {
    String pan;
    String nif;
    CardType cardType;
}
