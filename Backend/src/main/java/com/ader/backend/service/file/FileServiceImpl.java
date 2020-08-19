package com.ader.backend.service.file;

import com.ader.backend.entity.File;
import com.ader.backend.entity.Offer;
import com.ader.backend.entity.User;
import com.ader.backend.repository.FileRepository;
import com.ader.backend.service.offer.OfferService;
import com.ader.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

  private final FileRepository fileRepository;
  private final UserService userService;
  private OfferService offerService;

  @Autowired
  public void setOfferService(OfferService offerService) {
    this.offerService = offerService;
  }

  @Override
  public List<File> decompressFile(List<File> files) {
    List<File> decompressedImages = new ArrayList<>();
    files.forEach(image -> decompressedImages.add(decompressFile(image)));

    return decompressedImages;
  }

  @Override
  public File decompressFile(File file) {
    return new File(
            file.getOffer(),
            file.getUser(),
            file.getUuid(),
            file.getName(),
            file.getType(),
            decompressBytes(file.getBytes())
    );
  }

  @Override
  public List<File> compressFile(List<File> files) {
    List<File> compressedImages = new ArrayList<>();
    files.forEach(image -> compressedImages.add(compressFile(image)));

    return compressedImages;
  }

  @Override
  public File compressFile(File file) {
    return new File(
            file.getOffer(),
            file.getUser(),
            file.getUuid(),
            file.getName(),
            file.getType(),
            compressBytes(file.getBytes())
    );
  }

  // Uncompress the image bytes
  @Override
  public byte[] decompressBytes(byte[] data) {
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

  // Compress the image bytes before storing it in the database
  @Override
  public byte[] compressBytes(byte[] data) {
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

  @Override
  public List<File> getAllFiles() {
    return decompressFile(fileRepository.findAll());
  }

  @Override
  public File findByUuid(UUID uuid) {
    return fileRepository.findByUuid(uuid).orElse(null);
  }

  @Override
  public List<File> getAllFilesForOffer(Long offerId) {
    List<File> files = fileRepository.findAllByOffer_Id(offerId);
    return decompressFile(files);
  }

  @Override
  public List<File> getAllFilesForUser(String userEmail, String offerIsNull) {
    User user = userService.getUser(userEmail);
    List<File> filesFromDb = fileRepository.findAllFilesForUserWithoutOffer(user.getId());

    if (Boolean.parseBoolean(offerIsNull)) {
      if (filesFromDb.isEmpty()) {
        return new ArrayList<>();
      } else {
        return decompressFile(fileRepository.findAllFilesForUserWithoutOffer(user.getId()));
      }
    } else {
      return decompressFile(fileRepository.findAllByUser_Email(userEmail));
    }
  }

  @Override
  public File uploadFile(MultipartFile file, Long offerId) throws IOException {
    String errorMessage;
    String encodedName = Base64.getEncoder().encodeToString(
            Objects.requireNonNull(file.getOriginalFilename()).getBytes(StandardCharsets.UTF_8));

    UUID fileUuid = UUID.randomUUID();

    File newFile = new File(
            null,
            userService.getAuthenticatedUser(),
            fileUuid,
            encodedName,
            file.getContentType(),
            file.getBytes()
    );

    try {
      if (offerId != null) {
        Offer fileOffer = offerService.getOffer(offerId);

        // Compress the files again to avoid their corruption on file update
        List<File> files = new ArrayList<>(fileOffer.getFiles());
        fileOffer.getFiles().clear();
        fileOffer.getFiles().addAll(compressFile(files));

        newFile.setOffer(fileOffer);
        fileOffer.getFiles().add(compressFile(newFile));
      } else {
        fileRepository.save(compressFile(newFile));
      }
    } catch (Exception e) {
      errorMessage = e.getMessage();
      log.error(errorMessage);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    log.info("Successfully uploaded file: [UUID: {}, Name: {}, Type: {}, Size: {} bytes, User: {}]",
            newFile.getUuid(),
            newFile.getName(),
            newFile.getType(),
            newFile.getBytes().length,
            userService.getAuthentication().getName()
    );

    return newFile;
  }

  @Override
  public String deleteFile(UUID uuid) {
    String errorMessage;
    String successMessage;

    try {
      fileRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      errorMessage = e.getMessage();
      log.error(errorMessage);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    successMessage = "Successfully deleted file with UUID: [" + uuid + "]";
    log.info(successMessage);
    return successMessage;
  }

  @Override
  public File findByNameAndTypeAndUserId(String fileName, String fileType, Long userId) {
    return fileRepository.findByNameAndTypeAndUserId(fileName, fileType, userId);
  }
}
