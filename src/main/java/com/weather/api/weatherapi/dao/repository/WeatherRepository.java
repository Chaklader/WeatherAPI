package com.weather.api.weatherapi.dao.repository;

import com.weather.api.weatherapi.dao.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface WeatherRepository extends JpaRepository<WeatherData, String> {

//    Optional<Commodity> findCommodityByReference(String commodityReferenceId);
//
//    List<Commodity> findByCommodityStatusAndPurchaseDateBefore(Commodity.CommodityStatus commodityStatus, Date purchaseDate);
//
//    List<Commodity> findByCommodityStatusAndSellActivityIdIsNull(Commodity.CommodityStatus commodityStatus);
}