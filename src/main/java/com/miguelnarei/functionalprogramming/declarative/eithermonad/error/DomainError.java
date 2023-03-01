package com.miguelnarei.functionalprogramming.declarative.eithermonad.error;

import com.miguelnarei.functionalprogramming.declarative.Purchase;
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
