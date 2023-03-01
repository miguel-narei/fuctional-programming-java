package com.miguelnavarro.functionalprogramming.imperative;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.miguelnavarro.functionalprogramming.imperative.Purchase.Card;
import com.miguelnavarro.functionalprogramming.imperative.Purchase.CardType;
import com.miguelnavarro.functionalprogramming.imperative.exception.CardException;
import com.miguelnavarro.functionalprogramming.imperative.exception.DbException;
import com.miguelnavarro.functionalprogramming.imperative.exception.PersonException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ImperativeUseCaseTest {

    public static final String PAN = "1234123412341234";
    @Mock
    private ImperativeAdapter imperativeAdapter;

    @InjectMocks
    private ImperativeUseCase imperativeUseCase;

    @ParameterizedTest
    @CsvSource({
        "true,true,true,Ok",
        "false,true,true,Card Error",
        "true,false,true,Person Error",
        "true,true,false,Db Error"
    })
    void test(boolean successNif, boolean successPersonId, boolean successSave,
        String finalStatus) throws CardException, PersonException, DbException {

        mockService(successNif, successPersonId, successSave);

        Purchase purchase = Purchase.builder().amount(5.00).card(
            Card.builder().pan(PAN).build()).build();

        imperativeUseCase.storePurchase(purchase);
        assertThat(purchase.getStatus()).isEqualTo(finalStatus);

        if(purchase.getStatus().equals("Ok")) {
            assertThat(purchase.getCard().getCardType()).isEqualTo(CardType.CREDIT);
            assertThat(purchase.getFee()).isEqualTo(5.00 * 0.04);
        }
    }

    private void mockService(boolean successNif, boolean successPersonId, boolean successSave)
        throws CardException, PersonException, DbException {
        if (successNif) {
            when(imperativeAdapter.getCardDetails(anyString()))
                .thenReturn(new CardDetails(PAN, "12345678A", CardType.CREDIT));
        } else {
            when(imperativeAdapter.getCardDetails(anyString())).thenThrow(new CardException());
        }

        if (successPersonId) {
            lenient().when(imperativeAdapter.getPersonId(anyString())).thenReturn("0000000001");
        } else {
            when(imperativeAdapter.getPersonId(anyString())).thenThrow(new PersonException());
        }

        if (successSave) {
            when(imperativeAdapter.saveEntity(any())).thenAnswer(invocation -> invocation.getArgument(0));
        } else {
            when(imperativeAdapter.saveEntity(any())).thenThrow(new DbException());
        }
    }

}