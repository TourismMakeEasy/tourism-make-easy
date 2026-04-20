package com.tourism.service.booking.central.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

  private Long resortId;
  private String guestName;
  private String guestEmail;
  private LocalDate checkIn;
  private LocalDate checkOut;
  private int guests;
}
