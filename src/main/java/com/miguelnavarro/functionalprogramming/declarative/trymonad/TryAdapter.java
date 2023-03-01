package com.miguelnavarro.functionalprogramming.declarative.trymonad;

import com.miguelnavarro.functionalprogramming.declarative.CardDetails;
import com.miguelnavarro.functionalprogramming.declarative.Purchase;
import com.miguelnavarro.functionalprogramming.declarative.Purchase.CardType;
import com.miguelnavarro.functionalprogramming.imperative.exception.CardException;
import com.miguelnavarro.functionalprogramming.imperative.exception.DbException;
import com.miguelnavarro.functionalprogramming.imperative.exception.PersonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TryAdapter {

    CardDetails getCardDetails(String pan) throws CardException {
        log.info("Getting card details for pan {}", pan);
        return new CardDetails(pan, "12345678A", CardType.CREDIT);
    }

    String getPersonId(String nif) throws PersonException {
        log.info("Getting personId for nif {}", nif);
        return "000000001";
    }

    Purchase saveEntity(Purchase entity) throws DbException {
        log.info("Saving entity {}", entity);
        return entity;
    }


}
