package com.miguelnarei.functionalprogramming.declarative.async;

import com.miguelnarei.functionalprogramming.declarative.FeeStrategy;
import com.miguelnarei.functionalprogramming.declarative.Purchase;
import com.miguelnarei.functionalprogramming.declarative.Purchase.Card;
import com.miguelnarei.functionalprogramming.declarative.async.exception.CardException;
import com.miguelnarei.functionalprogramming.declarative.async.exception.DbException;
import com.miguelnarei.functionalprogramming.declarative.async.exception.PersonException;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AsyncTryUseCase {

    public final AsyncTryAdapter asyncTryAdapter;

    public CompletableFuture<Purchase> storePurchase(@NonNull String pan, double amount) {

        Purchase purchase = Purchase.builder()
            .card(Card.builder()
                .pan(pan)
                .build())
            .amount(amount).build();

        // Happy path
        return CompletableFuture.supplyAsync(() -> purchase)
            .thenCompose(this::getCardDetails)
            .thenCompose(this::getPersonId)
            .thenApply(this::applyFee)
            .thenApply(p -> p.withStatus("Ok"))
            .exceptionally(e -> {
                log.error("Error: ", e);
                if (e.getCause() instanceof CardException) {
                    return purchase.withStatus("Card Error");
                } else if (e.getCause() instanceof PersonException) {
                    return purchase.withStatus("Person Error");
                }
                throw (RuntimeException) e;
            })

            // Finally
            .thenCompose(this::saveEntity)

            // Log and return result
            .thenApply(p -> {
                log.info("Finally status is: {}", p);
                return p;
            })

            // Manage errors on final actions
            .exceptionally(t -> {
                // Manage errors
                log.error("Error: ", t);
                if (t.getCause() instanceof DbException) {
                    return purchase.withStatus("Db Error");
                }
                throw (RuntimeException) t;
            });

    }

    private CompletableFuture<Purchase> getCardDetails(Purchase o) {
        return asyncTryAdapter.getCardDetails(o.getCard().getPan())
            .thenApply(
                t -> o.withNif(t.getNif()).withCard(o.getCard().withCardType(t.getCardType())));
    }

    private CompletableFuture<Purchase> getPersonId(Purchase o) {
        return asyncTryAdapter.getPersonId(o.getNif())
            .thenApply(o::withPersonId);
    }

    private CompletableFuture<Purchase> saveEntity(Purchase o) {
        return asyncTryAdapter.saveEntity(o);
    }

    private Purchase applyFee(Purchase purchase) {
        return purchase.withFee(
            FeeStrategy.calculateFee(purchase.getCard().getCardType(), purchase.getAmount()));
    }

}
