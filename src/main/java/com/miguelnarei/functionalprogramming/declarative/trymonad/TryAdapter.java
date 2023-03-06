package com.miguelnarei.functionalprogramming.declarative.trymonad;

import com.miguelnarei.functionalprogramming.declarative.CardDetails;
import com.miguelnarei.functionalprogramming.declarative.Purchase;
import com.miguelnarei.functionalprogramming.declarative.Purchase.CardType;
import com.miguelnarei.functionalprogramming.imperative.exception.CardException;
import com.miguelnarei.functionalprogramming.imperative.exception.DbException;
import com.miguelnarei.functionalprogramming.imperative.exception.PersonException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TryAdapter {

    CardDetails getCardDetails(String pan) throws CardException {
        log.info("Getting card details for pan {}", pan);
        try {
            new CountDownLatch(1).await(800, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new CardException();
        }
        return new CardDetails(pan, "12345678A", CardType.CREDIT);
    }

    String getPersonId(String nif) throws PersonException {
        log.info("Getting personId for nif {}", nif);
        try {
            new CountDownLatch(1).await(300, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new PersonException();
        }
        return "000000001";
    }

    Purchase saveEntity(Purchase entity) throws DbException {
        log.info("Saving entity {}", entity);
        try {
            new CountDownLatch(1).await(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new DbException();
        }
        return entity;
    }


}
