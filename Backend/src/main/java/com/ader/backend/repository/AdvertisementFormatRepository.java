package com.ader.backend.repository;

import com.ader.backend.entity.AdvertisementFormat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvertisementFormatRepository extends JpaRepository<AdvertisementFormat, Long> {

    Optional<AdvertisementFormat> findByName(String name);
}
