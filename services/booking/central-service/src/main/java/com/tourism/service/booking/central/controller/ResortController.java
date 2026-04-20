package com.tourism.service.booking.central.controller;

import com.tourism.service.booking.central.dto.BookingRequest;
import com.tourism.service.booking.central.dto.BookingResponse;
import com.tourism.service.booking.central.dto.ResortDto;
import com.tourism.service.booking.central.entity.BookingEntity;
import com.tourism.service.booking.central.entity.ResortEntity;
import com.tourism.service.booking.central.repository.BookingRepository;
import com.tourism.service.booking.central.repository.ResortRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ResortController {

  private final ResortRepository resortRepository;
  private final BookingRepository bookingRepository;

  // ──── RESORT ENDPOINTS ────

  @GetMapping("/resorts")
  public ResponseEntity<List<ResortDto>> getAllResorts() {
    List<ResortDto> resorts = resortRepository.findAll().stream()
      .map(this::toDto)
      .toList();
    return ResponseEntity.ok(resorts);
  }

  @GetMapping("/resorts/{id}")
  public ResponseEntity<ResortDto> getResortById(@PathVariable Long id) {
    return resortRepository.findById(id)
      .map(this::toDto)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/resorts/search")
  public ResponseEntity<List<ResortDto>> searchResorts(
    @RequestParam(required = false) String location,
    @RequestParam(required = false) Double maxPrice
  ) {
    List<ResortEntity> results;

    if (location != null && maxPrice != null) {
      results = resortRepository
        .findByLocationContainingIgnoreCaseAndPricePerNightLessThanEqual(
          location,
          BigDecimal.valueOf(maxPrice)
        );
    } else if (location != null) {
      results = resortRepository.findByLocationContainingIgnoreCase(location);
    } else if (maxPrice != null) {
      results = resortRepository.findByPricePerNightLessThanEqual(
        BigDecimal.valueOf(maxPrice)
      );
    } else {
      results = resortRepository.findAll();
    }

    return ResponseEntity.ok(results.stream().map(this::toDto).toList());
  }

  // ──── BOOKING ENDPOINTS ────

  @PostMapping("/bookings")
  public ResponseEntity<BookingResponse> createBooking(
    @RequestBody BookingRequest request
  ) {
    Optional<ResortEntity> resortOpt = resortRepository.findById(request.getResortId());
    if (resortOpt.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    ResortEntity resort = resortOpt.get();
    long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
    if (nights <= 0) {
      return ResponseEntity.badRequest().build();
    }

    String bookingId = "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    BookingEntity booking = BookingEntity.builder()
      .bookingId(bookingId)
      .status("CONFIRMED")
      .resortId(resort.getId())
      .resortName(resort.getName())
      .guestName(request.getGuestName())
      .guestEmail(request.getGuestEmail())
      .checkIn(request.getCheckIn())
      .checkOut(request.getCheckOut())
      .guests(request.getGuests())
      .nights((int) nights)
      .totalPrice(resort.getPricePerNight().multiply(BigDecimal.valueOf(nights)))
      .createdAt(LocalDateTime.now())
      .build();

    bookingRepository.save(booking);
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(booking));
  }

  @GetMapping("/bookings/{bookingId}")
  public ResponseEntity<BookingResponse> getBooking(@PathVariable String bookingId) {
    return bookingRepository.findByBookingId(bookingId)
      .map(this::toResponse)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/bookings")
  public ResponseEntity<List<BookingResponse>> getAllBookings() {
    List<BookingResponse> bookings = bookingRepository.findAll().stream()
      .map(this::toResponse)
      .toList();
    return ResponseEntity.ok(bookings);
  }

  @DeleteMapping("/bookings/{bookingId}")
  public ResponseEntity<BookingResponse> cancelBooking(@PathVariable String bookingId) {
    Optional<BookingEntity> bookingOpt = bookingRepository.findByBookingId(bookingId);
    if (bookingOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    BookingEntity booking = bookingOpt.get();
    booking.setStatus("CANCELLED");
    bookingRepository.save(booking);
    return ResponseEntity.ok(toResponse(booking));
  }

  // ──── MAPPERS (temporary — will be replaced by MapStruct) ────

  private ResortDto toDto(ResortEntity entity) {
    return ResortDto.builder()
      .id(entity.getId())
      .name(entity.getName())
      .location(entity.getLocation())
      .description(entity.getDescription())
      .pricePerNight(entity.getPricePerNight())
      .rating(entity.getRating())
      .amenities(
        entity.getAmenities() != null
          ? List.of(entity.getAmenities().split(","))
          : List.of()
      )
      .imageUrl(entity.getImageUrl())
      .build();
  }

  private BookingResponse toResponse(BookingEntity entity) {
    return BookingResponse.builder()
      .bookingId(entity.getBookingId())
      .status(entity.getStatus())
      .resortId(entity.getResortId())
      .resortName(entity.getResortName())
      .guestName(entity.getGuestName())
      .guestEmail(entity.getGuestEmail())
      .checkIn(entity.getCheckIn())
      .checkOut(entity.getCheckOut())
      .guests(entity.getGuests())
      .nights(entity.getNights())
      .totalPrice(entity.getTotalPrice())
      .createdAt(entity.getCreatedAt())
      .build();
  }
}
