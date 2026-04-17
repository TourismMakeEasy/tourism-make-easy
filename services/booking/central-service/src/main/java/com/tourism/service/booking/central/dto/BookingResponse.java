package com.tourism.service.booking.central.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

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
