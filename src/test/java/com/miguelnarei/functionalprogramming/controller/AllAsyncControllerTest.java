package com.miguelnarei.functionalprogramming.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.miguelnarei.functionalprogramming.controller.model.PurchaseDto;
import com.miguelnarei.functionalprogramming.controller.model.PurchaseResonseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class AllAsyncControllerTest {

    @Test
    void syncTest() {
        PurchaseDto request = PurchaseDto.builder()
            .pan("1234123412341234")
            .amount(5.00)
            .build();

        ResponseEntity<PurchaseResonseDto> response = new TestRestTemplate().postForEntity("http://localhost:8080/all-async/purchases",
            request, PurchaseResonseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

}