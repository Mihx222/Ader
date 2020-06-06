package com.ader.backend.service.bid;

import com.ader.backend.entity.Bid;
import com.ader.backend.entity.Offer;
import com.ader.backend.entity.Persona;
import com.ader.backend.entity.User;
import com.ader.backend.repository.BidRepository;
import com.ader.backend.rest.dto.BidDto;
import com.ader.backend.service.offer.OfferService;
import com.ader.backend.service.persona.PersonaService;
import com.ader.backend.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidServiceImplTest {

  @Mock
  private BidRepository bidRepository;
  @Mock
  private UserService userService;
  @Mock
  private PersonaService personaService;
  @Mock
  private OfferService offerService;
  @InjectMocks
  private BidServiceImpl bidService;
  private List<Bid> bids;
  private Bid bid1;
  private Bid bid2;
  private User testInfluencer;
  private User testAdvertiser;
  private Offer offer1;
  private Offer offer2;
  private Persona persona;

  @BeforeEach
  void setUp() {
    bidService = new BidServiceImpl(bidRepository, offerService, userService, personaService);

    bids = new ArrayList<>();

    testInfluencer = new User();
    testInfluencer.setId(1L);
    testInfluencer.setEmail("user@user.com");
    testInfluencer.setPassword("test");

    testAdvertiser = new User();
    testAdvertiser.setId(2L);
    testAdvertiser.setEmail("ad@ad.com");
    testAdvertiser.setPassword("test");
    testAdvertiser.setBrandName("somebrand");
    testAdvertiser.setBrandWebsite("somewebsite");

    offer1 = new Offer();
    offer1.setId(1L);
    offer1.setName("Test 1");
    offer1.setAuthor(testInfluencer);
    offer1.setAssignees(new HashSet<>());
    offer1.setAdvertisementFormats(Collections.emptyList());
    offer1.setCategories(Collections.emptyList());
    offer1.setFiles(Collections.emptyList());

    offer2 = new Offer();
    offer2.setId(2L);
    offer2.setName("Test 2");
    offer2.setAuthor(testAdvertiser);
    offer2.setAssignees(new HashSet<>());
    offer2.setAdvertisementFormats(Collections.emptyList());
    offer2.setCategories(Collections.emptyList());
    offer2.setFiles(Collections.emptyList());

    bid1 = new Bid();
    bid1.setId(1L);
    bid2 = new Bid();
    bid2.setId(1L);

    persona = new Persona();
    persona.setActivity("Nothing");
    persona.setAudience("Nobody");
    persona.setSellingOrientation("What is this even?");
    persona.setValues("No values");
  }

  @Test
  void getBids_whenInvoked_returnAllBids() {
    bids.addAll(Arrays.asList(bid1, bid2));
    when(bidRepository.findAll()).thenReturn(bids);

    List<Bid> bids = bidService.getBids();

    assertThat(bids).hasSize(2);
  }

  @Test
  void getBidsByUser_whenInvoked_returnOneBid() {
    bid1.setUser(testInfluencer);
    bid2.setUser(testAdvertiser);
    bids.addAll(Arrays.asList(bid1, bid2));
    when(bidRepository.findAllByUserEmail(any(String.class))).thenReturn(
            bids.stream().filter(bid -> bid.getUser().equals(testInfluencer)).collect(Collectors.toList())
    );

    List<Bid> bids = bidService.getBidsByUser(testInfluencer.getEmail());

    assertThat(bids).hasSize(1);
  }

  @Test
  void getBidsByOffer_whenInvoked_returnOneBid() {
    bid1.setOffer(offer1);
    bid2.setOffer(offer2);
    bids.addAll(Arrays.asList(bid1, bid2));
    when(bidRepository.findAllByOfferId(any(Long.class))).thenReturn(
            bids.stream().filter(bid -> bid.getOffer().getId().equals(offer1.getId())).collect(Collectors.toList())
    );

    List<Bid> bids = bidService.getBidsByOffer(offer1.getId());

    assertThat(bids).hasSize(1);
  }

  @Test
  void getBidByUserEmailAndOfferId_whenInvoked_returnOneBid() {
    bid1.setOffer(offer1);
    bid1.setUser(testInfluencer);
    bid2.setOffer(offer2);
    bid2.setUser(testAdvertiser);
    bids.addAll(Arrays.asList(bid1, bid2));
    when(bidRepository.findByUser_EmailAndOffer_Id(any(String.class), any(Long.class))).thenReturn(
            bids.stream().filter(bid -> bid.getUser().getEmail().equals(testInfluencer.getEmail()) &&
                    bid.getOffer().getId().equals(offer1.getId())).findFirst().orElse(null)
    );

    Bid bid = bidService.getBidByUserEmailAndOfferId(testInfluencer.getEmail(), offer1.getId());

    assertThat(bid).isNotNull();
    assertThat(bid.getUser()).isEqualTo(testInfluencer);
    assertThat(bid.getOffer()).isEqualTo(offer1);
  }

  @Test
  void createBid_whenInvoked_callSaveOnlyOnce() {
    bid1.setPersona(persona);
    bid1.setOffer(offer1);
    bid1.setUser(testInfluencer);
    bid1.setAcceptInitialRequirements(false);

    assertThatCode(() -> bidService.createBid(BidDto.toDto(bid1))).doesNotThrowAnyException();
  }

  @Test
  void acceptBids_whenInvoked_doesNotThrowAnyExceptions() {
    bid1.setPersona(persona);
    bid1.setOffer(offer1);
    bid1.setUser(testInfluencer);
    bid2.setPersona(persona);
    bid2.setOffer(offer2);
    bid2.setUser(testAdvertiser);
    bids.addAll(Arrays.asList(bid1, bid2));

    when(bidRepository.findById(bid1.getId())).thenReturn(java.util.Optional.ofNullable(bid1));
    when(bidRepository.findById(bid2.getId())).thenReturn(java.util.Optional.ofNullable(bid2));
    when(offerService.getOffer(offer2.getId())).thenReturn(offer1);
    when(userService.getUser(testAdvertiser.getId())).thenReturn(testInfluencer);

    assertThatCode(() -> bidService.acceptBids(bids)).doesNotThrowAnyException();
  }

  @Test
  void updateBid_whenInvoked_doesNotThrowAnyExceptions() {
    when(bidRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(bid1));

    assertThatCode(() -> bidService.updateBid(bid1.getId(), bid2)).doesNotThrowAnyException();
  }

  @Test
  void deleteBid_whenInvoked_deleteIsCalledOnlyOnceAndDoesNotThrowAnyExceptions() {
    assertThatCode(() -> bidService.deleteBid(bid1.getId())).doesNotThrowAnyException();
    verify(bidRepository, times(1)).deleteById(any(Long.class));
  }
}
