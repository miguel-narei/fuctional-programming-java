package com.miguelnavarro.functionalprogramming.declarative.trymonad;

import com.miguelnavarro.functionalprogramming.declarative.Purchase;
import com.miguelnavarro.functionalprogramming.declarative.FeeStrategy;
import com.miguelnavarro.functionalprogramming.declarative.Purchase.Card;
import com.miguelnavarro.functionalprogramming.imperative.exception.CardException;
import com.miguelnavarro.functionalprogramming.imperative.exception.DbException;
import com.miguelnavarro.functionalprogramming.imperative.exception.PersonException;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TryUseCase {

    public final TryAdapter tryAdapter;

    public Purchase storePurchase(@NonNull String pan, double amount) {

        Purchase purchase = Purchase.builder()
            .card(Card.builder()
                .pan(pan)
                .build())
            .amount(amount).build();

        // Happy path
        return Try.of(() -> purchase)
            .mapTry(this::getCardDetails)
            .mapTry(this::getPersonId)
            .map(this::applyFee)
            .map(o -> o.withStatus("Ok"))

            // Manage errors
            .onFailure(t -> log.error("Error: ", t))
            .recover(CardException.class, purchase.withStatus("Card Error"))
            .recover(PersonException.class, purchase.withStatus("Person Error"))

            // Finally
            .mapTry(this::saveEntity)

            // Manage errors on final actions
            .onFailure(t -> log.error("Error: ", t))
            .recover(DbException.class, purchase.withStatus("Db Error"))

            // Log and return result
            .onSuccess(s -> log.info("Finally status is: {}", s))
            .get();

    }

    private Purchase getCardDetails(Purchase o) throws CardException {
        return Option.of(tryAdapter.getCardDetails(o.getCard().getPan()))
            .map(t -> o.withNif(t.getNif()).withCard(o.getCard().withCardType(t.getCardType()))).get();
    }

    private Purchase getPersonId(Purchase o) throws PersonException {
        return o.withPersonId(tryAdapter.getPersonId(o.getNif()));
    }

    private Purchase saveEntity(Purchase o) throws DbException {
        tryAdapter.saveEntity(o);
        return o;
    }

    private Purchase applyFee(Purchase purchase) {
        return purchase.withFee(FeeStrategy.calculateFee(purchase.getCard().getCardType(),purchase.getAmount()));
    }

}
