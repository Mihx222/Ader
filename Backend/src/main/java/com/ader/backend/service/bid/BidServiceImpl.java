package com.ader.backend.service.bid;

import com.ader.backend.entity.bid.Bid;
import com.ader.backend.entity.bid.BidDto;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.BidRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BidServiceImpl implements BidService {

    private static final Logger log = LoggerFactory.getLogger(BidServiceImpl.class);
    private final BidRepository bidRepository;

    public BidServiceImpl(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    @Override
    public ResponseEntity<List<BidDto>> getBids() {
        return ResponseEntity.ok(BidDto.toDto(bidRepository.findAll()));
    }

    @Override
    public ResponseEntity<List<BidDto>> getBidsByUser(String userEmail) {
        return ResponseEntity.ok(BidDto.toDto(bidRepository.findAllByUserEmail(userEmail)));
    }

    @Override
    public ResponseEntity<List<BidDto>> getBidsByOffer(Long offerId) {
        return ResponseEntity.ok(BidDto.toDto(bidRepository.findAllByOfferId(offerId)));
    }

    @Override
    public ResponseEntity<Object> createBid(Bid bid) {
        String errorMessage;

        try {
            bidRepository.save(bid);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        log.info("Successfully created bid: [{}]", bid);
        return ResponseEntity.ok(BidDto.toDto(bid));
    }

    @Override
    public ResponseEntity<Object> updateBid(Long id, Bid bid) {
        String errorMessage;
        Bid bidToUpdate = bidRepository.findById(id).orElse(null);

        if (bidToUpdate == null) {
            errorMessage = "Bid with id: [" + id + "] does not exist!";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
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
                return ResponseEntity.badRequest().body(errorMessage);
            }
        }

        log.info("Successfully updated bid. New bid: [{}]", bidToUpdate);
        return ResponseEntity.ok(BidDto.toDto(bidToUpdate));
    }

    @Override
    public ResponseEntity<Object> deleteBid(Long id) {
        String errorMessage;

        try {
            bidRepository.deleteById(id);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        String successMessage = "Successfully deleted bid with id: [" + id + "]";
        log.info(successMessage);
        return ResponseEntity.ok(successMessage);
    }
}
