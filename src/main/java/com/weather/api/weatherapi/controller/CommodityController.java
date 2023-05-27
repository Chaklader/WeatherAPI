package com.weather.api.weatherapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import tech.seedz.los.controller.commodity.dto.CommodityDto;
//import tech.seedz.los.controller.commodity.dto.CommodityOwnershipDecisionDto;
//import tech.seedz.los.service.commodity.CommodityService;
//
//import javax.validation.Valid;

@SuppressWarnings("LineLength")
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/commodities")
public class CommodityController {

//    private final CommodityService commodityService;
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CommodityDto> findById(@PathVariable String id) {
//        log.info("Query commodity details by id {}", id);
//        return ResponseEntity.ok(commodityService.findDtoById(id));
//    }
//
//    @PutMapping("/{id}/ownership")
//    public ResponseEntity<CommodityDto> commoditySell(@PathVariable(name = "id") String commodityId, @RequestBody @Valid CommodityOwnershipDecisionDto ownershipDecisionDto) {
//        log.info("Selling commodity for commodityId: {}", commodityId);
//        return ResponseEntity.ok(commodityService.sellOrKeepCommodityOnClientRequest(commodityId, ownershipDecisionDto.getDecision()));
//    }

}