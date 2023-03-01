package com.miguelnavarro.functionalprogramming.imperative;

import com.miguelnavarro.functionalprogramming.imperative.Purchase.CardType;
import com.miguelnavarro.functionalprogramming.imperative.exception.CardException;
import com.miguelnavarro.functionalprogramming.imperative.exception.DbException;
import com.miguelnavarro.functionalprogramming.imperative.exception.PersonException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ImperativeUseCase {

    public final ImperativeAdapter imperativeAdapter;

    public void storePurchase(@NonNull Purchase purchase) {

        try {
            getCardDetails(purchase);

            getPersonId(purchase);

            double fee;
            if (purchase.getCard().getCardType() == CardType.CREDIT) {
                if (purchase.getAmount() < 100) {
                    fee = purchase.getAmount() * 0.04;
                } else {
                    fee = purchase.getAmount() * 0.02;
                }
            } else {
                if (purchase.getAmount() < 100) {
                    fee = purchase.getAmount() * 0.06;
                } else {
                    fee = purchase.getAmount() * 0.03;
                }
            }
            purchase.setFee(fee);

            purchase.setStatus("Ok");


        } catch (CardException e) {
            log.error("Error: ", e);
            purchase.setStatus("Card Error");
        } catch (PersonException e) {
            log.error("Error: ", e);
            purchase.setStatus("Person Error");

        } finally {
            try {
                saveEntity(purchase);
            } catch (DbException e) {
                log.error("Error: ", e);
                purchase.setStatus("Db Error");
            }
            log.info("Finally status is: {}", purchase.getStatus());
        }
    }

    private void getCardDetails(Purchase o) throws CardException {
        CardDetails tuple2 = imperativeAdapter.getCardDetails(o.getCard().getPan());
        o.setNif(tuple2.getNif());
        o.getCard().setCardType(tuple2.getCardType());
    }

    private void getPersonId(Purchase o) throws PersonException {
        String personId = imperativeAdapter.getPersonId(o.getNif());
        o.setPersonId(personId);
    }

    private void saveEntity(Purchase o) throws DbException {
        imperativeAdapter.saveEntity(o);
    }

}
