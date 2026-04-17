package com.tourism.service.booking.central.controller;

import com.tourism.service.booking.central.dto.BookingRequest;
import com.tourism.service.booking.central.dto.BookingResponse;
import com.tourism.service.booking.central.dto.ResortDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class ResortController {

  private final Map<Long, ResortDto> resorts = new ConcurrentHashMap<>();
  private final Map<String, BookingResponse> bookings = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  public ResortController() {
    // Seed with mock data
    resorts.put(
      1L,
      ResortDto.builder()
        .id(1L)
        .name("Taj Lake Palace")
        .location("Udaipur, Rajasthan")
        .description(
          "A stunning luxury hotel floating on Lake Pichola, offering breathtaking views and royal heritage."
        )
        .pricePerNight(new BigDecimal("25000.00"))
        .rating(4.9)
        .amenities(List.of("Pool", "Spa", "Lake View", "Fine Dining", "Boat Transfer"))
        .imageUrl("https://example.com/taj-lake-palace.jpg")
        .build()
    );

    resorts.put(
      2L,
      ResortDto.builder()
        .id(2L)
        .name("Kumarakom Lake Resort")
        .location("Kumarakom, Kerala")
        .description(
          "A luxurious lakeside resort nestled amid the tranquil Kerala backwaters."
        )
        .pricePerNight(new BigDecimal("18000.00"))
        .rating(4.7)
        .amenities(List.of("Houseboat", "Ayurveda Spa", "Infinity Pool", "Backwater Cruise"))
        .imageUrl("https://example.com/kumarakom.jpg")
        .build()
    );

    resorts.put(
      3L,
      ResortDto.builder()
        .id(3L)
        .name("Wildflower Hall")
        .location("Shimla, Himachal Pradesh")
        .description(
          "A former residence of Lord Kitchener set amid 22 acres of cedar forest in the Himalayas."
        )
        .pricePerNight(new BigDecimal("22000.00"))
        .rating(4.8)
        .amenities(List.of("Mountain View", "Heated Pool", "Trekking", "Spa", "Jacuzzi"))
        .imageUrl("https://example.com/wildflower-hall.jpg")
        .build()
    );
  }

  // ──── RESORT ENDPOINTS ────

  @GetMapping("/resorts")
  public ResponseEntity<List<ResortDto>> getAllResorts() {
    return ResponseEntity.ok(new ArrayList<>(resorts.values()));
  }

  @GetMapping("/resorts/{id}")
  public ResponseEntity<ResortDto> getResortById(@PathVariable Long id) {
    ResortDto resort = resorts.get(id);
    if (resort == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(resort);
  }

  @GetMapping("/resorts/search")
  public ResponseEntity<List<ResortDto>> searchResorts(
    @RequestParam(required = false) String location,
    @RequestParam(required = false) Double maxPrice
  ) {
    List<ResortDto> results = resorts.values().stream()
      .filter(r -> location == null || r.getLocation().toLowerCase().contains(location.toLowerCase()))
      .filter(r -> maxPrice == null || r.getPricePerNight().doubleValue() <= maxPrice)
      .toList();
    return ResponseEntity.ok(results);
  }

  // ──── BOOKING ENDPOINTS ────

  @PostMapping("/bookings")
  public ResponseEntity<BookingResponse> createBooking(
    @RequestBody BookingRequest request
  ) {
    ResortDto resort = resorts.get(request.getResortId());
    if (resort == null) {
      return ResponseEntity.badRequest().build();
    }

    long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
    if (nights <= 0) {
      return ResponseEntity.badRequest().build();
    }

    String bookingId = "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    BookingResponse booking = BookingResponse.builder()
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

    bookings.put(bookingId, booking);
    return ResponseEntity.status(HttpStatus.CREATED).body(booking);
  }

  @GetMapping("/bookings/{bookingId}")
  public ResponseEntity<BookingResponse> getBooking(@PathVariable String bookingId) {
    BookingResponse booking = bookings.get(bookingId);
    if (booking == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(booking);
  }

  @GetMapping("/bookings")
  public ResponseEntity<List<BookingResponse>> getAllBookings() {
    return ResponseEntity.ok(new ArrayList<>(bookings.values()));
  }

  @DeleteMapping("/bookings/{bookingId}")
  public ResponseEntity<BookingResponse> cancelBooking(@PathVariable String bookingId) {
    BookingResponse booking = bookings.get(bookingId);
    if (booking == null) {
      return ResponseEntity.notFound().build();
    }
    booking.setStatus("CANCELLED");
    return ResponseEntity.ok(booking);
  }
}
