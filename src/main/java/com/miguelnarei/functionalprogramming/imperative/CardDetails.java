package com.miguelnarei.functionalprogramming.imperative;

import com.miguelnarei.functionalprogramming.imperative.Purchase.CardType;
import lombok.Value;

@Value
public class CardDetails {
    String pan;
    String nif;
    CardType cardType;
}
