package com.miguelnavarro.functionalprogramming.declarative;

import com.miguelnavarro.functionalprogramming.declarative.Purchase.CardType;
import lombok.Value;

@Value
public class CardDetails {
    String pan;
    String nif;
    CardType cardType;
}
