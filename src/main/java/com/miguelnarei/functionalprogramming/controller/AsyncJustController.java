package com.miguelnarei.functionalprogramming.controller;

import com.miguelnarei.functionalprogramming.declarative.trymonad.TryUseCase;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AsyncJustController {

    private final TryUseCase tryUseCase;

    @PostMapping("/async-just-controller/purchases")
    public CompletableFuture<ResponseEntity<PurchaseResonseDto>> createPurchase(
        @RequestBody PurchaseDto purchaseDto) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.CREATED).body(
            PurchaseMapper.fromPurchase(
                tryUseCase.storePurchase(purchaseDto.getPan(), purchaseDto.getAmount()))));
    }
}
