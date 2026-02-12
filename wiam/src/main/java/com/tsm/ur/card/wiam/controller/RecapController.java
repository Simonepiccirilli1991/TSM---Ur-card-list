package com.tsm.ur.card.wiam.controller;


import com.tsm.ur.card.wiam.model.BaseRecap;
import com.tsm.ur.card.wiam.model.request.RecapRequest;
import com.tsm.ur.card.wiam.service.recap.RecapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/recap")
public class RecapController {

    private final RecapService recapService;


    @PostMapping("getrecap")
    public ResponseEntity<List<BaseRecap>> getRecap(@RequestBody RecapRequest request){
        return ResponseEntity.ok(recapService.getRecap(request));
    }
}
