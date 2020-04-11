package com.ader.backend.service.offer;

import com.ader.backend.entity.Status;
import com.ader.backend.entity.offer.Offer;
import com.ader.backend.entity.offer.OfferDto;
import com.ader.backend.entity.role.Roles;
import com.ader.backend.entity.user.User;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.OfferRepository;
import com.ader.backend.repository.RoleRepository;
import com.ader.backend.service.user.UserService;
import com.ader.backend.service.user.UserServiceImpl;
import liquibase.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

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
    public ResponseEntity<List<OfferDto>> getAllOffers() {
        return ResponseEntity.ok(OfferDto.toDto(offerRepository.findAll()));
    }

    @Override
    public ResponseEntity<OfferDto> getOffer(Long id) {
        Offer fetchedOffer = offerRepository.findById(id).orElse(null);

        if (fetchedOffer == null) {
            String errorMessage = "No offer found for id: [" + id + "]";
            log.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        } else {
            return ResponseEntity.ok(OfferDto.toDto(fetchedOffer));
        }
    }

    @Override
    public ResponseEntity<OfferDto> createOffer(Offer offer) throws DatabaseException {
        Offer newOffer;

        if (offer.getStatus() == null) {
            offer.setStatus(Status.ACTIVE);
        }

        try {
            newOffer = offerRepository.save(offer);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException(ex);
        }

        log.info("Created new offer: {}", newOffer.toString());
        return ResponseEntity.ok(
                OfferDto.toDto(Objects.requireNonNull(offerRepository.findById(newOffer.getId()).orElse(null)))
        );
    }

    @Override
    public ResponseEntity<OfferDto> updateOffer(Long id, Offer offer) {
        User authenticatedUser = userService.getAuthenticatedUser();
        Offer offerToUpdate = offerRepository.findById(id).orElse(null);
        String errorMessage;

        if (offerToUpdate == null) {
            errorMessage = "Offer with id [" + id + "] not found!";
            log.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        } else {
            if ((!authenticatedUser.getCreatedOffers().isEmpty() &&
                    authenticatedUser.getCreatedOffers().contains(offerToUpdate)) ||
                    authenticatedUser.getRoles().contains(roleRepository.findByName(Roles.ROLE_ADMIN.toString()))) {
                BeanUtils.copyProperties(
                        offer,
                        offerToUpdate,
                        BeanHelper.getNullPropertyNames(offer, true)
                );

                log.info("Updated offer with id: [{}]", id);
                return ResponseEntity.ok(OfferDto.toDto(offerToUpdate));
            } else {
                errorMessage = "You do not have rights to update offer with id: [" + id + "]!";
                log.error(errorMessage);
                throw new AccessDeniedException(errorMessage);
            }
        }
    }

    @Override
    public ResponseEntity<String> deleteOffer(Long id) {
        Offer offerToDelete = offerRepository.findById(id).orElse(null);

        if (offerToDelete == null) {
            String errorMessage = "Offer with id: [" + id + "] not found!";
            log.error(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }

        offerToDelete.setStatus(Status.DELETED);

        log.info("Deleted offer with id: [{}]", id);
        return ResponseEntity.ok("Deleted offer with id: [" + id + "]");
    }
}
