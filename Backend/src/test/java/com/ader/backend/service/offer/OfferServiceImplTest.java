package com.ader.backend.service.offer;

import com.ader.backend.entity.*;
import com.ader.backend.repository.OfferRepository;
import com.ader.backend.service.advertisementformat.AdvertisementFormatService;
import com.ader.backend.service.bid.BidService;
import com.ader.backend.service.category.CategoryService;
import com.ader.backend.service.file.FileService;
import com.ader.backend.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OfferServiceImplTest {

  @Mock
  private Authentication authentication;
  @Mock
  private SecurityContext securityContext;
  @Mock
  private UserService userService;
  @Mock
  private OfferRepository offerRepository;
  @Mock
  private CategoryService categoryService;
  @Mock
  private FileService fileService;
  @Mock
  private AdvertisementFormatService advertisementFormatService;
  @Mock
  private BidService bidService;

  @InjectMocks
  private OfferServiceImpl offerService;

  private User testInfluencer;
  private User testAdvertiser;
  private List<Offer> offers;
  private Authentication mockAuth;
  private Offer offer1;
  private Offer offer2;

  @BeforeEach
  void setUp() {
    offerService = new OfferServiceImpl(offerRepository, userService, categoryService, fileService, advertisementFormatService);
    offerService.setBidService(bidService);

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

    offers = new ArrayList<>();
    offer1 = new Offer();
    offer1.setId(1L);
    offer1.setName("Test 1");
    offer1.setAuthor(testInfluencer);
    offer1.setAssignees(Collections.emptySet());
    offer1.setAdvertisementFormats(Collections.emptyList());
    offer1.setCategories(Collections.emptyList());
    offer1.setFiles(Collections.emptyList());

    offer2 = new Offer();
    offer2.setId(2L);
    offer2.setName("Test 2");
    offer2.setAuthor(testAdvertiser);
    offer2.setAssignees(Collections.emptySet());
    offer2.setAdvertisementFormats(Collections.emptyList());
    offer2.setCategories(Collections.emptyList());
    offer2.setFiles(Collections.emptyList());

    Role role = new Role();
    role.setName(Roles.ROLE_USER.name());
    mockAuth = new OAuth2Authentication(
            new OAuth2Request(
                    Collections.emptyMap(),
                    "mockClient",
                    Collections.emptyList(),
                    true,
                    new HashSet<>(Arrays.asList("read", "write")),
                    Collections.emptySet(),
                    null,
                    Collections.emptySet(),
                    Collections.emptyMap()),
            new UsernamePasswordAuthenticationToken(
                    testInfluencer.getEmail(),
                    "N/A",
                    Collections.singletonList(new SimpleGrantedAuthority(role.getName())))
    );
  }

  @Test
  void getAllOffer_whenInvoked_returnAllOffers() {
    offers.addAll(Arrays.asList(offer1, offer2));
    when(offerRepository.findAllByOfferStatus(OfferStatus.OPEN.getName())).thenReturn(offers);

    List<Offer> offers = offerService.getAllOffers();
    assertThat(offers).hasSize(2);
  }

  @Test
  void getAllByUser_whenInvoked_returnAllOffersCreatedByUser() {
    offers.addAll(Arrays.asList(offer1, offer2));
    when(offerRepository.findAllByAuthor_Email(testInfluencer.getEmail())).thenReturn(
            offers.stream().filter(offer -> offer.getAuthor().getEmail().equals(testInfluencer.getEmail())).collect(Collectors.toList())
    );

    List<Offer> offers = offerService.getAllForUser(testInfluencer.getEmail());

    assertThat(offers).hasSize(1);
    assertThat(offers.get(0).getAuthor()).isEqualTo(testInfluencer);
  }

  @Test
  void getById_whenInvoked_returnOffer() {
    offers.addAll(Arrays.asList(offer1, offer2));
    when(offerRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(offer1));

    Offer offer = offerService.getOffer(1L);

    assertThat(offer).isNotNull();
    assertThat(offer).isEqualTo(offer1);
  }

  @Test
  void getByUserEmailAndBidsExist_whenInvoked_returnEmptyList() {
    offers.addAll(Arrays.asList(offer1, offer2));
    when(offerRepository.findAllByUser_EmailAndBidsExist(testInfluencer.getEmail())).thenReturn(
            offers.stream().filter(offer -> offer.getAuthor().getEmail().equals(testInfluencer.getEmail()) &&
                    !offer.getBids().isEmpty()).collect(Collectors.toList())
    );

    List<Offer> offers = offerService.getByUserEmailAndBidsExist(testInfluencer.getEmail());

    assertThat(offers).isEmpty();
  }

  @Test
  void getAllByAssignedUserEmail_whenInvoked_returnOffer() {
    offer1.setAssignees(new HashSet<>(Arrays.asList(testInfluencer, testAdvertiser)));
    offers.addAll(Arrays.asList(offer1, offer2));
    when(offerRepository.findAllByAssignedUser(testInfluencer.getEmail())).thenReturn(
            offers.stream().filter(offer -> offer.getAssignees().contains(testInfluencer)).collect(Collectors.toList())
    );

    List<Offer> offers = offerService.getAllByAssignedUserEmail(testInfluencer.getEmail());

    assertThat(offers).hasSize(1);
    assertThat(offers.get(0)).isEqualTo(offer1);
  }

  @Test
  void getAllCompletedForUser_whenInvoked_returnOffers() {
    offer1.setAssignees(new HashSet<>(Arrays.asList(testInfluencer, testAdvertiser)));
    offer1.setOfferStatus(OfferStatus.COMPLETED);
    offers.addAll(Arrays.asList(offer1, offer2));
    when(offerRepository.findAllCompletedForUser(testInfluencer.getEmail())).thenReturn(
            offers.stream().filter(offer -> offer.getAssignees().contains(testInfluencer) &&
                    offer.getOfferStatus().equals(OfferStatus.COMPLETED)).collect(Collectors.toList())
    );

    List<Offer> offers = offerService.getAllCompletedForUser(testInfluencer.getEmail());

    assertThat(offers).hasSize(1);
    assertThat(offers.get(0)).isEqualTo(offer1);
  }

  @Test
  void createOffer_whenInvoked_CallSaveOnce() {
    offerService.createOffer(offer1);

    verify(offerRepository, times(1)).save(offer1);
  }

  @Test
  void updateOffer_whenInvoked_noThrownExceptions() {
    testInfluencer.setCreatedOffers(Collections.singletonList(offer1));

    SecurityContextHolder.setContext(securityContext);
    when(userService.getAuthenticatedUser()).thenReturn(testInfluencer);
    when(offerRepository.findById(offer1.getId())).thenReturn(Optional.ofNullable(offer1));
    when(offerRepository.findById(offer2.getId())).thenReturn(Optional.ofNullable(offer2));

    offerService.updateOffer(offer1.getId(), offer2);

    assertThatCode(() -> offerService.updateOffer(offer1.getId(), offer2)).doesNotThrowAnyException();
  }

  @Test
  void deassignFromOffer_whenInvoked_removeAssigneeFromOffer() {
    Bid bid = new Bid();
    bid.setUser(testInfluencer);

    offer1.setAssignees(new HashSet<>(Arrays.asList(testInfluencer, testAdvertiser)));
    offers.addAll(Arrays.asList(offer1, offer2));
    when(userService.getUser(any(String.class))).thenReturn(testInfluencer);
    when(offerRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(offer1));
    when(bidService.getBidByUserEmailAndOfferId(any(String.class), any(Long.class))).thenReturn(bid);

    assertThatCode(() -> offerService.deassignFromOffer(
            testInfluencer.getEmail(), offer1.getId().toString(), BidStatus.CANCELED.getName())
    ).doesNotThrowAnyException();
  }

  @Test
  void updateOfferStatus_whenInvoked_noThrownExceptions() {
    when(offerRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(offer1));

    assertThatCode(() -> offerService.updateOfferStatus(offer1.getId(), OfferStatus.ASSIGNED.getName())).doesNotThrowAnyException();
  }

  @Test
  void deleteOffer_whenInvoked_noThrownExceptions() {
    when(offerRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(offer1));

    assertThatCode(() -> offerService.deleteOffer(offer1.getId())).doesNotThrowAnyException();
  }

  // Hz
  @Test
  void checkAndUpdateExpiredOffers_whenInvoked_Hz() {

  }
}
