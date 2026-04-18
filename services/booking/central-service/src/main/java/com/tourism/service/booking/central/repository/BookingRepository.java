package com.tourism.service.booking.central.repository;

import com.tourism.service.booking.central.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * TEMPORARY test repository — will be replaced with proper schema.
 */
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

  Optional<BookingEntity> findByBookingId(String bookingId);
}
