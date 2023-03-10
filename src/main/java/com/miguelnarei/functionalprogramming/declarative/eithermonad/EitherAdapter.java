package com.miguelnarei.functionalprogramming.declarative.eithermonad;

import com.miguelnarei.functionalprogramming.declarative.eithermonad.error.InfrastructureError;
import com.miguelnarei.functionalprogramming.declarative.CardDetails;
import com.miguelnarei.functionalprogramming.declarative.Purchase;
import com.miguelnarei.functionalprogramming.declarative.Purchase.CardType;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EitherAdapter {

    Either<InfrastructureError, CardDetails> getCardDetails(String pan) {
        log.info("Getting card details for pan {}", pan);
        return Either.right(new CardDetails(pan, "12345678A", CardType.CREDIT));
    }

    Either<InfrastructureError, String> getPersonId(String nif) {
        log.info("Getting personId for nif {}", nif);
        return Either.right("000000001");
    }

    Either<InfrastructureError, Purchase> saveEntity(Purchase entity) {
        log.info("Saving entity {}", entity);
        return Either.right(entity);
    }


}
