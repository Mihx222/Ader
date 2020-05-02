package com.ader.backend.service.offerimage;

import com.ader.backend.entity.OfferImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OfferImageService {

    List<OfferImage> getAllOfferMedia();

    List<OfferImage> getAllOfferMediaForOffer(Long offerId);

    OfferImage uploadImage(MultipartFile file) throws IOException;

    String deleteImage(Long id);
}
