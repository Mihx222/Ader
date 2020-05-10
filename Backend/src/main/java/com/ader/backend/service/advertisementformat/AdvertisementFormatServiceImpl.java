package com.ader.backend.service.advertisementformat;

import com.ader.backend.entity.AdvertisementFormat;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.AdvertisementFormatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AdvertisementFormatServiceImpl implements AdvertisementFormatService {

    private static final String ADVERTISEMENT_FORMAT_WITH_ID = "Advertisement format with id: [";
    private static final String ADVERTISEMENT_FORMAT_WITH_NAME = "Advertisement format with name: [";
    private static final String NOT_FOUND_ERROR_MESSAGE = "] not found";

    private final AdvertisementFormatRepository advertisementFormatRepository;

    @Override
    public List<AdvertisementFormat> getAllAdvertisementFormats() {
        return advertisementFormatRepository.findAll();
    }

    @Override
    public AdvertisementFormat getAdvertisementFormat(Long id) {
        AdvertisementFormat advertisementFormat = advertisementFormatRepository.findById(id).orElse(null);
        String errorMessage;

        if (advertisementFormat == null) {
            errorMessage = ADVERTISEMENT_FORMAT_WITH_ID + id + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        return advertisementFormat;
    }

    @Override
    public AdvertisementFormat getAdvertisementFormat(String name) {
        AdvertisementFormat advertisementFormat = advertisementFormatRepository.findByName(name).orElse(null);
        String errorMessage;

        if (advertisementFormat == null) {
            errorMessage = ADVERTISEMENT_FORMAT_WITH_NAME + name + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        return advertisementFormat;
    }

    @Override
    public AdvertisementFormat createAdvertisementFormat(AdvertisementFormat advertisementFormat) {
        String errorMessage;

        try {
            advertisementFormatRepository.save(advertisementFormat);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        log.info("Created advertisement format: [{}]", advertisementFormat);
        return advertisementFormat;
    }

    @Override
    public AdvertisementFormat updateAdvertisementFormat(Long id, AdvertisementFormat advertisementFormat) {
        String errorMessage;
        AdvertisementFormat advertisementFormatToUpdate = advertisementFormatRepository.findById(id).orElse(null);

        if (advertisementFormatToUpdate == null) {
            errorMessage = ADVERTISEMENT_FORMAT_WITH_ID + id + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        try {
            BeanUtils.copyProperties(
                    advertisementFormat,
                    advertisementFormatToUpdate,
                    BeanHelper.getNullPropertyNames(advertisementFormat, true)
            );

            advertisementFormatRepository.save(advertisementFormat);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    errorMessage
            );
        }

        log.info("Advertisement format [{}] updated to [{}]", advertisementFormat, advertisementFormatToUpdate);
        return advertisementFormatToUpdate;
    }

    @Override
    public AdvertisementFormat updateAdvertisementFormat(String name, AdvertisementFormat advertisementFormat) {
        String errorMessage;
        AdvertisementFormat advertisementFormatToUpdate = advertisementFormatRepository.findByName(name).orElse(null);

        if (advertisementFormatToUpdate == null) {
            errorMessage = ADVERTISEMENT_FORMAT_WITH_NAME + name + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        } else {
            return updateAdvertisementFormat(advertisementFormatToUpdate.getId(), advertisementFormat);
        }
    }

    @Override
    public String deleteAdvertisementFormat(Long id) {
        String errorMessage;
        AdvertisementFormat advertisementFormatToDelete = advertisementFormatRepository.findById(id).orElse(null);

        if (advertisementFormatToDelete == null) {
            errorMessage = ADVERTISEMENT_FORMAT_WITH_ID + id + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        try {
            advertisementFormatRepository.delete(advertisementFormatToDelete);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    errorMessage
            );
        }

        String successMessage = "Advertisement format with id: [{" + id + "}] successfully deleted";
        log.info(successMessage);
        return successMessage;
    }

    @Override
    public String deleteAdvertisementFormat(String name) {
        String errorMessage;
        AdvertisementFormat advertisementFormatToDelete = advertisementFormatRepository.findByName(name).orElse(null);

        if (advertisementFormatToDelete == null) {
            errorMessage = ADVERTISEMENT_FORMAT_WITH_ID + name + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        } else {
            return deleteAdvertisementFormat(advertisementFormatToDelete.getId());
        }
    }
}
