package com.ader.backend.service.offerimage;

import com.ader.backend.entity.OfferImage;
import com.ader.backend.repository.OfferImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OfferImageServiceImpl implements OfferImageService {

    private final OfferImageRepository offerImageRepository;

    // Compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        log.debug("Original Image Byte Size - {}", data.length);
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.debug("Compressed Image Byte Size - {}", outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    // Uncompress the image bytes
    public static byte[] decompressBytes(byte[] data) {
        log.debug("Compressed Image Byte Size - {}", data.length);
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ioe) {
            log.error(ioe.getMessage());
        }

        log.debug("Decompressed Image Byte Size - {}", outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    public static OfferImage compressImage(OfferImage offerImage) {
        return new OfferImage(
                offerImage.getName(),
                offerImage.getType(),
                compressBytes(offerImage.getPicByte())
        );
    }

    public static List<OfferImage> compressImage(List<OfferImage> offerImages) {
        List<OfferImage> compressedImages = new ArrayList<>();
        offerImages.forEach(image -> compressedImages.add(compressImage(image)));

        return compressedImages;
    }

    public static OfferImage decompressImage(OfferImage offerImage) {
        return new OfferImage(
                offerImage.getName(),
                offerImage.getType(),
                decompressBytes(offerImage.getPicByte())
        );
    }

    public static List<OfferImage> decompressImage(List<OfferImage> offerImages) {
        List<OfferImage> decompressedImages = new ArrayList<>();
        offerImages.forEach(image -> decompressedImages.add(decompressImage(image)));

        return decompressedImages;
    }

    @Override
    public List<OfferImage> getAllOfferMedia() {
        return decompressImage(offerImageRepository.findAll());
    }

    @Override
    public List<OfferImage> getAllOfferMediaForOffer(Long offerId) {
        return decompressImage(offerImageRepository.findAllByOfferId(offerId));
    }

    @Override
    public OfferImage uploadImage(MultipartFile file) throws IOException {
        String errorMessage;

        OfferImage image = new OfferImage(
                Base64.getEncoder().encodeToString(
                        Objects.requireNonNull(file.getOriginalFilename()).getBytes(StandardCharsets.UTF_8)
                ),
                file.getContentType(),
                file.getBytes()
        );

        try {
            offerImageRepository.save(compressImage(image));
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        log.info("Successfully uploaded image: [Name: {}, Type: {}, Size: {}]",
                image.getName(), image.getType(), image.getPicByte().length
        );
        return image;
    }

    @Override
    public String deleteImage(Long id) {
        String errorMessage;
        String successMessage;

        try {
            offerImageRepository.deleteById(id);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        successMessage = "Successfully deleted offer media with id: [" + id + "]";
        log.info(successMessage);
        return successMessage;
    }
}
