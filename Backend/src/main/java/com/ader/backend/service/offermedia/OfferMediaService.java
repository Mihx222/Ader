package com.ader.backend.service.offermedia;

import com.ader.backend.entity.OfferMedia;

import java.util.List;

public interface OfferMediaService {

    List<OfferMedia> getAllOfferMedia();

    List<OfferMedia> getAllOfferMediaForOffer(Long offerId);

    OfferMedia createOfferMedia(OfferMedia offerMedia);

    OfferMedia updateOfferMedia(Long id, OfferMedia offerMedia);

    String deleteOfferMedia(Long id);
}
