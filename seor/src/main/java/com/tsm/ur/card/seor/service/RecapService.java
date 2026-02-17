package com.tsm.ur.card.seor.service;

import com.tsm.ur.card.seor.model.response.BaseRecap;
import com.tsm.ur.card.seor.service.wiam.WiamIntegration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecapService {

    private final WiamIntegration wiamIntegration;

    public List<BaseRecap> getRecap(String username) {
        log.info("RecapService - getRecap per username (JWT): {}", username);
        return wiamIntegration.getRecap(username);
    }
}

