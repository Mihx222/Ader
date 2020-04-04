package com.ader.backend.service.impl;

import com.ader.backend.entity.Offer;
import com.ader.backend.entity.Roles;
import com.ader.backend.entity.Status;
import com.ader.backend.entity.User;
import com.ader.backend.entity.dto.OfferDto;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.OfferRepository;
import com.ader.backend.repository.RoleRepository;
import com.ader.backend.service.OfferService;
import com.ader.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class OfferServiceImpl implements OfferService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final OfferRepository offerRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;

    public OfferServiceImpl(OfferRepository offerRepository, UserService userService, RoleRepository roleRepository) {
        this.offerRepository = offerRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<OfferDto> getAllOffers() {
        return OfferDto.toDto(offerRepository.findAll());
    }

    @Override
    public ResponseEntity<OfferDto> getOffer(Long id) {
        Offer fetchedOffer = offerRepository.findById(id).orElse(null);

        if (fetchedOffer == null) {
            log.error("No offer found for id: [{}]", id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(OfferDto.toDto(fetchedOffer));
        }
    }

    @Override
    public OfferDto createOffer(Offer offer) {
        if (offer.getStatus() == null) {
            offer.setStatus(Status.ACTIVE);
        }

        try {
            offerRepository.save(offer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        log.info("Created new offer: {}", offer.toString());
//        return OfferDto.toDto(Objects.requireNonNull(offerRepository.findById(offer.getId()).orElse(null)));
        return OfferDto.toDto(offer);
    }

    @Override
    public ResponseEntity<OfferDto> updateOffer(Long id, Offer offer) {
        User authenticatedUser = userService.getAuthenticatedUser();
        Offer offerToUpdate = offerRepository.findById(id).orElse(null);

        if (offerToUpdate == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            if (authenticatedUser.getOffers().contains(offerToUpdate) ||
                    authenticatedUser.getRoles().contains(roleRepository.findByName(Roles.ROLE_ADMIN.toString()))) {
                BeanUtils.copyProperties(
                        offer,
                        offerToUpdate,
                        BeanHelper.getNullPropertyNames(offer, true)
                );
            } else {
                return new ResponseEntity<>(OfferDto.toDto(offer), HttpStatus.FORBIDDEN);
            }
        }

        log.info("Updated offer with id: [{}]", id);
        return ResponseEntity.ok(OfferDto.toDto(offerToUpdate));
    }

    @Override
    public ResponseEntity<String> deleteOffer(Long id) {
        Offer offerToDelete = offerRepository.findById(id).orElse(null);

        if (offerToDelete == null) {
            log.error("Offer with id: [{}] not found!", id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        try {
            offerRepository.deleteById(id);
            log.info("Deleted offer with id: [{}]", id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(
                    "An error occurred when deleting entity with id: [" + id + "]",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return ResponseEntity.ok("Removed offer with id: [" + id + "]");
    }
}
