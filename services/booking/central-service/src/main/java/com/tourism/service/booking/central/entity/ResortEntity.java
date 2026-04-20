package com.tourism.service.booking.central.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * TEMPORARY test entity — will be replaced when proper ER diagram is designed.
 */
@Entity
@Table(name = "resorts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResortEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String location;

  @Column(columnDefinition = "TEXT")
  private String description;

  private BigDecimal pricePerNight;

  private double rating;

  private String amenities; // stored as comma-separated for simplicity

  private String imageUrl;
}
