package com.miguelnarei.functionalprogramming.declarative;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.API.Tuple;
import static java.util.function.Predicate.not;

import com.miguelnarei.functionalprogramming.declarative.Purchase.CardType;
import io.vavr.Tuple2;
import java.util.function.Predicate;

public class FeeStrategy {

    private static final Predicate<Tuple2<CardType, Double>> isDebit =
        tuple -> tuple._1 == CardType.DEBIT;
    private static final Predicate<Tuple2<CardType, Double>> isCredit = not(isDebit);
    private static final Predicate<Tuple2<CardType, Double>> isGreaterEqualsThan100 =
        tuple -> tuple._2 >= 100;
    private static final Predicate<Tuple2<CardType, Double>> isLowerThan100 = not(
        isGreaterEqualsThan100);

    public static Double calculateFee(final CardType cardType, final Double amount) {
        return Match(Tuple(cardType, amount)).of(
            Case($(isCredit.and(isLowerThan100)), () -> (amount * 0.04)),
            Case($(isCredit.and(isGreaterEqualsThan100)), () -> amount * 0.02),
            Case($(isDebit.and(isLowerThan100)), () -> amount * 0.06),
            Case($(isDebit.and(isGreaterEqualsThan100)), () -> amount * 0.03),
            Case($(), () -> null)
        );
    }
}