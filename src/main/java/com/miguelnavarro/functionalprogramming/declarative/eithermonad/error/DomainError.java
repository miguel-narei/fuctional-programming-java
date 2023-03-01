package com.miguelnavarro.functionalprogramming.declarative.eithermonad.error;

import com.miguelnavarro.functionalprogramming.declarative.Purchase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@AllArgsConstructor
@Builder
@With
public class DomainError {

    String code;
    String message;
    Purchase purchase;
}
