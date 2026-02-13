package com.tsm.ur.card.seor.service;

import com.tsm.ur.card.seor.model.request.RecapRequest;
import com.tsm.ur.card.seor.model.response.BaseRecap;
import com.tsm.ur.card.seor.service.wiam.WiamIntegration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecapService {

    private final WiamIntegration wiamIntegration;

    public Mono<List<BaseRecap>> getRecap(RecapRequest request) {
        log.info("RecapService - getRecap per username: {}", request.username());
        return wiamIntegration.getRecap(request);
    }
}

