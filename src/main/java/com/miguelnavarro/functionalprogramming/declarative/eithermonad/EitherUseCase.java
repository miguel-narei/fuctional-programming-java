package com.miguelnavarro.functionalprogramming.declarative.eithermonad;

import com.miguelnavarro.functionalprogramming.declarative.CardDetails;
import com.miguelnavarro.functionalprogramming.declarative.FeeStrategy;
import com.miguelnavarro.functionalprogramming.declarative.Purchase;
import com.miguelnavarro.functionalprogramming.declarative.Purchase.Card;
import com.miguelnavarro.functionalprogramming.declarative.Purchase.CardType;
import com.miguelnavarro.functionalprogramming.declarative.eithermonad.error.DomainError;
import com.miguelnavarro.functionalprogramming.declarative.eithermonad.error.InfrastructureError;
import io.vavr.control.Either;
import java.util.function.Function;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EitherUseCase {

    public final EitherAdapter eitherAdapter;

    public Either<DomainError, Purchase> storePurchase(@NonNull String pan, double amount) {

        // Happy path
        return
            // Collect Impure Inputs
            getCardDetails(pan)
                .flatMap(cardDetails -> getPersonId(cardDetails.getNif())
                    // Calculate pure business logic
                    .map(personId -> {
                            final Double fee = getFee(cardDetails.getCardType(), amount);
                            return buildPurchaseOk(pan, amount, cardDetails.getNif(),
                                cardDetails.getCardType(), personId, fee);
                        }
                    )
                )

                // Manage errors
                .mapLeft(e -> e.withPurchase(
                    buildPurchaseError(pan, amount, e.getMessage()))
                )

                // Finally
                .bimap(this::saveEntityError, this::saveEntity)
                .getOrElseGet(e -> e)

                // Manage errors on final actions
                .mapLeft(e -> e.withPurchase(
                    buildPurchaseError(pan, amount, e.getMessage())))

                // Log and return result
                .peekLeft(e -> log.error("Error: {}", e))
                .peek(s -> log.info("Finally status is: {}", s));

    }

    private Purchase buildPurchaseOk(String pan, double amount, String nif, CardType cardType,
        String personId, Double fee) {
        return Purchase.builder()
            .card(Card.builder()
                .pan(pan)
                .cardType(cardType)
                .build())
            .amount(amount)
            .nif(nif)
            .personId(personId)
            .fee(fee)
            .status("Ok")
            .build();
    }

    private Purchase buildPurchaseError(String pan, double amount, String status) {
        return Purchase.builder()
            .card(Card.builder()
                .pan(pan)
                .build())
            .amount(amount)
            .status(status)
            .build();
    }

    private Either<DomainError, CardDetails> getCardDetails(String pan) {
        return eitherAdapter.getCardDetails(pan)
            .mapLeft(toDomainError());
    }

    private Either<DomainError, String> getPersonId(String nif) {
        return eitherAdapter.getPersonId(nif)
            .mapLeft(toDomainError());
    }

    private Either<DomainError, Purchase> saveEntity(Purchase purchase) {
        return eitherAdapter.saveEntity(purchase)
            .mapLeft(toDomainError());
    }

    private Either<DomainError, Purchase> saveEntityError(DomainError domainError) {
        return saveEntity(domainError.getPurchase())
            // Transform right into left because we come from an error
            .flatMap(saved -> Either.left(domainError.withPurchase(saved)));
    }

    private Double getFee(CardType cardType, double amount) {
        return FeeStrategy.calculateFee(cardType, amount);
    }

    private static Function<InfrastructureError, DomainError> toDomainError() {
        return e -> DomainError.builder()
            .code(e.getCode())
            .message(e.getMessage())
            .build();
    }

}
