package com.miguelnarei.functionalprogramming.imperative;

import com.miguelnarei.functionalprogramming.imperative.exception.DbException;
import com.miguelnarei.functionalprogramming.imperative.exception.CardException;
import com.miguelnarei.functionalprogramming.imperative.exception.PersonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImperativeAdapter {

    public CardDetails getCardDetails(String pan) throws CardException {
        log.info("Getting card details for pan {}", pan);
        return new CardDetails(pan, "12345678A", Purchase.CardType.CREDIT);
    }

    public String getPersonId(String nif) throws PersonException {
        log.info("Getting personId for nif {}", nif);
        return "000000001";
    }

    public Purchase saveEntity(Purchase entity) throws DbException {
        log.info("Saving entity {}", entity);
        return entity;
    }


}
