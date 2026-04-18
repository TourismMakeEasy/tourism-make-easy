package com.tourism.service.booking.central;

import com.tourism.service.booking.central.entity.ResortEntity;
import com.tourism.service.booking.central.repository.ResortRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * Seeds demo resort data on startup if the resorts table is empty.
 * TEMPORARY — will be removed when proper data migration is in place.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

  private final ResortRepository resortRepository;

  @Override
  public void run(String... args) {
    if (resortRepository.count() > 0) {
      log.info("Resorts already seeded, skipping.");
      return;
    }

    log.info("Seeding demo resort data...");

    resortRepository.save(
      ResortEntity.builder()
        .name("Taj Lake Palace")
        .location("Udaipur, Rajasthan")
        .description(
          "A stunning luxury hotel floating on Lake Pichola, offering breathtaking views and royal heritage."
        )
        .pricePerNight(new BigDecimal("25000.00"))
        .rating(4.9)
        .amenities("Pool,Spa,Lake View,Fine Dining,Boat Transfer")
        .imageUrl("https://example.com/taj-lake-palace.jpg")
        .build()
    );

    resortRepository.save(
      ResortEntity.builder()
        .name("Kumarakom Lake Resort")
        .location("Kumarakom, Kerala")
        .description(
          "A luxurious lakeside resort nestled amid the tranquil Kerala backwaters."
        )
        .pricePerNight(new BigDecimal("18000.00"))
        .rating(4.7)
        .amenities("Houseboat,Ayurveda Spa,Infinity Pool,Backwater Cruise")
        .imageUrl("https://example.com/kumarakom.jpg")
        .build()
    );

    resortRepository.save(
      ResortEntity.builder()
        .name("Wildflower Hall")
        .location("Shimla, Himachal Pradesh")
        .description(
          "A former residence of Lord Kitchener set amid 22 acres of cedar forest in the Himalayas."
        )
        .pricePerNight(new BigDecimal("22000.00"))
        .rating(4.8)
        .amenities("Mountain View,Heated Pool,Trekking,Spa,Jacuzzi")
        .imageUrl("https://example.com/wildflower-hall.jpg")
        .build()
    );

    log.info("Seeded 3 demo resorts.");
  }
}
