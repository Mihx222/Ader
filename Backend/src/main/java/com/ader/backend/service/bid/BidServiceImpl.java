package com.ader.backend.service.bid;

import com.ader.backend.entity.Bid;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;

    @Override
    public List<Bid> getBids() {
        return bidRepository.findAll();
    }

    @Override
    public List<Bid> getBidsByUser(String userEmail) {
        return bidRepository.findAllByUserEmail(userEmail);
    }

    @Override
    public List<Bid> getBidsByOffer(Long offerId) {
        return bidRepository.findAllByOfferId(offerId);
    }

    @Override
    public Bid createBid(Bid bid) {
        String errorMessage;

        try {
            bidRepository.save(bid);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    errorMessage
            );
        }

        log.info("Successfully created bid: [{}]", bid);
        return bid;
    }

    @Override
    public Bid updateBid(Long id, Bid bid) {
        String errorMessage;
        Bid bidToUpdate = bidRepository.findById(id).orElse(null);

        if (bidToUpdate == null) {
            errorMessage = "Bid with id: [" + id + "] does not exist!";
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        } else {
            try {
                BeanUtils.copyProperties(
                        bid,
                        bidToUpdate,
                        BeanHelper.getNullPropertyNames(bid, true)
                );
            } catch (Exception e) {
                errorMessage = e.getMessage();
                log.error(errorMessage);
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        errorMessage
                );
            }
        }

        log.info("Successfully updated bid. New bid: [{}]", bidToUpdate);
        return bidToUpdate;
    }

    @Override
    public String deleteBid(Long id) {
        String errorMessage;

        try {
            bidRepository.deleteById(id);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        String successMessage = "Successfully deleted bid with id: [" + id + "]";
        log.info(successMessage);
        return successMessage;
    }
}
