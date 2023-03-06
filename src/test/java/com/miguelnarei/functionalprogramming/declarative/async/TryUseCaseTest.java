package com.miguelnarei.functionalprogramming.declarative.async;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.miguelnarei.functionalprogramming.declarative.CardDetails;
import com.miguelnarei.functionalprogramming.declarative.Purchase.CardType;
import com.miguelnarei.functionalprogramming.declarative.async.exception.CardException;
import com.miguelnarei.functionalprogramming.declarative.async.exception.DbException;
import com.miguelnarei.functionalprogramming.declarative.async.exception.PersonException;
import io.vavr.control.Try;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TryUseCaseTest {

    public static final String PAN = "1234123412341234";
    @Mock
    private AsyncTryAdapter tryAdapter;

    @InjectMocks
    private AsyncTryUseCase tryUseCase;

    @ParameterizedTest
    @CsvSource({
        "true,true,true,Ok",
        "false,true,true,Card Error",
        "true,false,true,Person Error",
        "true,true,false,Db Error"
    })
    void test(boolean successNif, boolean successPersonId, boolean successSave, String finalStatus) {

        mockService(successNif, successPersonId, successSave);

        Try.of(() -> tryUseCase.storePurchase(PAN, 5.00))
            .map(CompletableFuture::join)
            .onFailure(__ -> fail("Should never fail here"))
            .peek(response -> assertThat(response.getStatus()).isEqualTo(finalStatus))
            .filter(p -> "Ok".equals(p.getStatus()))
            .peek(
                purchase -> assertThat(purchase.getCard().getCardType()).isEqualTo(CardType.CREDIT))
            .peek(purchase -> assertThat(purchase.getFee()).isEqualTo(5.00 * 0.04));
    }

    private void mockService(boolean successNif, boolean successPersonId, boolean successSave) {
        if (successNif) {
            when(tryAdapter.getCardDetails(anyString()))
                .thenReturn(CompletableFuture.completedFuture(
                    new CardDetails(PAN, "12345678A", CardType.CREDIT)));
        } else {
            when(tryAdapter.getCardDetails(anyString())).thenThrow(new CardException());
        }

        if (successPersonId) {
            lenient().when(tryAdapter.getPersonId(anyString()))
                .thenReturn(CompletableFuture.completedFuture("0000000001"));
        } else {
            when(tryAdapter.getPersonId(anyString())).thenThrow(new PersonException());
        }

        if (successSave) {
            when(tryAdapter.saveEntity(any())).thenAnswer(
                invocation -> CompletableFuture.completedFuture(invocation.getArgument(0)));
        } else {
            when(tryAdapter.saveEntity(any())).thenThrow(new DbException());
        }
    }

}