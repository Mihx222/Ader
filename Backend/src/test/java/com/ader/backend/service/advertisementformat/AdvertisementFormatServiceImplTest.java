package com.ader.backend.service.advertisementformat;

import com.ader.backend.entity.AdvertisementFormat;
import com.ader.backend.repository.AdvertisementFormatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdvertisementFormatServiceImplTest {

  @Mock
  private AdvertisementFormatRepository advertisementFormatRepository;

  @InjectMocks
  private AdvertisementFormatServiceImpl advertisementFormatService;

  private List<AdvertisementFormat> advertisementFormats;
  private AdvertisementFormat format1;
  private AdvertisementFormat format2;

  @BeforeEach
  void setUp() {
    advertisementFormatService = new AdvertisementFormatServiceImpl(advertisementFormatRepository);

    advertisementFormats = new ArrayList<>();

    format1 = new AdvertisementFormat();
    format1.setId(1L);
    format1.setName("Test 1");
    format2 = new AdvertisementFormat();
    format2.setId(1L);
    format2.setName("Test 2");
  }

  @Test
  void getAllAdvertisementFormats_whenInvoked_returnAllAdvertisementFormats() {
    advertisementFormats.addAll(Arrays.asList(format1, format2));
    when(advertisementFormatRepository.findAll()).thenReturn(advertisementFormats);

    List<AdvertisementFormat> formats = advertisementFormatService.getAllAdvertisementFormats();

    assertThat(formats).hasSize(2);
  }

  @Test
  void getAdvertisementFormat_whenInvoked_returnAdvertisementFormat() {
    advertisementFormats.addAll(Arrays.asList(format1, format2));
    when(advertisementFormatRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(format1));
    when(advertisementFormatRepository.findByName(any(String.class))).thenReturn(java.util.Optional.ofNullable(format2));

    AdvertisementFormat byId = advertisementFormatService.getAdvertisementFormat(format1.getId());
    AdvertisementFormat byName = advertisementFormatService.getAdvertisementFormat(format2.getName());

    assertThat(byId).isNotNull();
    assertThat(byId).isEqualTo(format1);
    assertThat(byName).isNotNull();
    assertThat(byName).isEqualTo(format2);
  }

  @Test
  void createAdvertisementFormat_whenInvoked_callSaveOneTime() {
    advertisementFormatService.createAdvertisementFormat(format1);

    verify(advertisementFormatRepository, times(1)).save(format1);
  }

  @Test
  void updateAdvertisementFormat_whenInvoked_doesNotThrowAnyException() {
    when(advertisementFormatRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(format1));
    when(advertisementFormatRepository.findByName(any(String.class))).thenReturn(java.util.Optional.ofNullable(format2));

    assertThatCode(() -> advertisementFormatService.updateAdvertisementFormat(format1.getId(), format2)).doesNotThrowAnyException();
    assertThatCode(() -> advertisementFormatService.updateAdvertisementFormat(format2.getName(), format1)).doesNotThrowAnyException();
  }

  @Test
  void deleteAdvertisementFormat_whenInvoked_callDeleteOnlyOnce() {
    when(advertisementFormatRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(format1));
    when(advertisementFormatRepository.findByName(any(String.class))).thenReturn(java.util.Optional.ofNullable(format2));

    assertThatCode(() -> advertisementFormatService.deleteAdvertisementFormat(format1.getId())).doesNotThrowAnyException();
    assertThatCode(() -> advertisementFormatService.deleteAdvertisementFormat(format2.getName())).doesNotThrowAnyException();
  }
}
