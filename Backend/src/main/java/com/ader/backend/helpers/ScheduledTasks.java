package com.ader.backend.helpers;

import com.ader.backend.service.offer.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final OfferService offerService;

    @Scheduled(fixedDelay = 10000)
    public void offerStatusCheck() {
        offerService.checkAndUpdateExpiredOffers();
    }
}
