package com.ader.backend.service.advertisementformat;

import com.ader.backend.entity.AdvertisementFormat;

import java.util.List;

public interface AdvertisementFormatService {

    List<AdvertisementFormat> getAllAdvertisementFormats();

    AdvertisementFormat getAdvertisementFormat(Long id);

    AdvertisementFormat getAdvertisementFormat(String name);

    AdvertisementFormat createAdvertisementFormat(AdvertisementFormat advertisementFormat);

    AdvertisementFormat updateAdvertisementFormat(Long id, AdvertisementFormat advertisementFormat);

    AdvertisementFormat updateAdvertisementFormat(String name, AdvertisementFormat advertisementFormat);

    String deleteAdvertisementFormat(Long id);

    String deleteAdvertisementFormat(String name);
}
