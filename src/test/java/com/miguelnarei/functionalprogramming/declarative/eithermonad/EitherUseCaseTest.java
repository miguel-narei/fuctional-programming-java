package com.miguelnarei.functionalprogramming.declarative.eithermonad;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.miguelnarei.functionalprogramming.declarative.CardDetails;
import com.miguelnarei.functionalprogramming.declarative.Purchase.CardType;
import com.miguelnarei.functionalprogramming.declarative.eithermonad.error.InfrastructureError;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class EitherUseCaseTest {

    public static final String PAN = "1234123412341234";
    @Mock
    private EitherAdapter eitherAdapter;

    @InjectMocks
    private EitherUseCase eitherUseCase;

    @ParameterizedTest
    @CsvSource({
        "true,true,true,Ok",
        "false,true,true,Nif Error",
        "true,false,true,Person Error",
        "true,true,false,Db Error"
    })
    void test(boolean successNif, boolean successPersonId, boolean successSave,
        String finalStatus) {

        mockService(successNif, successPersonId, successSave);

        eitherUseCase.storePurchase(PAN, 5.00)
            .peekLeft(error -> {
                Assertions.assertThat(error.getPurchase().getStatus()).isEqualTo(finalStatus);
                Assertions.assertThat(error.getCode()).isNotNull();
                Assertions.assertThat(error.getMessage()).isEqualTo(finalStatus);
            })
            .peek(right -> {
                Assertions.assertThat(right.getStatus()).isEqualTo(finalStatus);
                Assertions.assertThat(right.getCard().getCardType()).isEqualTo(CardType.CREDIT);
                Assertions.assertThat(right.getFee()).isEqualTo(5.00 * 0.04);
            });

    }

    private void mockService(boolean successNif, boolean successPersonId, boolean successSave) {
        when(eitherAdapter.getCardDetails(anyString())).thenReturn(
            successNif ? Either.right(new CardDetails(PAN, "12345678A", CardType.CREDIT))
                : Either.left(new InfrastructureError("COD-001", "Nif Error")));

        lenient().when(eitherAdapter.getPersonId(anyString()))
            .thenReturn(successPersonId ? Either.right("0000000001")
                : Either.left(new InfrastructureError("COD-002", "Person Error")));

        when(eitherAdapter.saveEntity(any())).thenAnswer(invocation ->
            successSave ? Either.right(invocation.getArgument(0))
                : Either.left(new InfrastructureError("COD-003", "Db Error")));
    }

}