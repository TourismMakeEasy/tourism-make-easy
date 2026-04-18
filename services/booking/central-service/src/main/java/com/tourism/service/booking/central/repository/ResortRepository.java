package com.tourism.service.booking.central.repository;

import com.tourism.service.booking.central.entity.ResortEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * TEMPORARY test repository — will be replaced with proper schema.
 */
public interface ResortRepository extends JpaRepository<ResortEntity, Long> {

  List<ResortEntity> findByLocationContainingIgnoreCase(String location);

  List<ResortEntity> findByPricePerNightLessThanEqual(java.math.BigDecimal maxPrice);

  List<ResortEntity> findByLocationContainingIgnoreCaseAndPricePerNightLessThanEqual(
    String location,
    java.math.BigDecimal maxPrice
  );
}
