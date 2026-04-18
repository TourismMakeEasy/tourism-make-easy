package com.tourism.service.booking.central.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TEMPORARY test entity — will be replaced when proper ER diagram is designed.
 */
@Entity
@Table(name = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String bookingId;

  private String status;

  private Long resortId;

  private String resortName;

  private String guestName;

  private String guestEmail;

  private LocalDate checkIn;

  private LocalDate checkOut;

  private int guests;

  private int nights;

  private BigDecimal totalPrice;

  private LocalDateTime createdAt;
}
