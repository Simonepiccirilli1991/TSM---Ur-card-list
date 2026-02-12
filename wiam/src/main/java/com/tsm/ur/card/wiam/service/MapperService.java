package com.tsm.ur.card.wiam.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@Slf4j
public class MapperService {


    ObjectMapper mapper = new ObjectMapper();


    public Object mappaDtoSuRecap(Object dto, Class<?> recapClass){
        try {
            var recap = mapper.convertValue(dto, recapClass);
            log.info("Mapped DTO to Recap: {}", recap);
            return recap;
        } catch (Exception e) {
            log.error("Error mapping DTO to Recap", e);
            throw new RuntimeException("Mapping error", e);
        }
    }
}
