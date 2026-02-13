package com.tsm.ur.card.seor.controller;

import com.tsm.ur.card.seor.model.request.RecapRequest;
import com.tsm.ur.card.seor.model.response.BaseRecap;
import com.tsm.ur.card.seor.service.RecapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/recap")
public class RecapController {

    private final RecapService recapService;

    @PostMapping("/getrecap")
    public Mono<ResponseEntity<List<BaseRecap>>> getRecap(@RequestBody RecapRequest request) {
        return recapService.getRecap(request)
                .map(ResponseEntity::ok);
    }
}

