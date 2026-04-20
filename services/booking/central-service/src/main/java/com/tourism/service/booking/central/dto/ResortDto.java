package com.tourism.service.booking.central.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResortDto {

  private Long id;
  private String name;
  private String location;
  private String description;
  private BigDecimal pricePerNight;
  private double rating;
  private List<String> amenities;
  private String imageUrl;
}
