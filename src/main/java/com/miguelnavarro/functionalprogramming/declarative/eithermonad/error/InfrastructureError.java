package com.miguelnavarro.functionalprogramming.declarative.eithermonad.error;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class InfrastructureError {

    String code;
    String message;
}
