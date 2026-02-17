package com.tsm.ur.card.seor.service;

import com.tsm.ur.card.seor.model.request.VendiProdottoRequest;
import com.tsm.ur.card.seor.model.response.BaseResponse;
import com.tsm.ur.card.seor.service.wiam.WiamIntegration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VenditaService {

    private final WiamIntegration wiamIntegration;

    public BaseResponse vendiProdotto(String username, VendiProdottoRequest request) {
        log.info("VenditaService - vendiProdotto per username (JWT): {}, idProdotto: {}, tipo: {}",
                username, request.idProdotto(), request.tipoProdotto());
        return wiamIntegration.vendiProdotto(username, request);
    }
}

