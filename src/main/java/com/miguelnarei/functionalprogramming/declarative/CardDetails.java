package com.miguelnarei.functionalprogramming.declarative;

import com.miguelnarei.functionalprogramming.declarative.Purchase.CardType;
import lombok.Value;

@Value
public class CardDetails {
    String pan;
    String nif;
    CardType cardType;
}
