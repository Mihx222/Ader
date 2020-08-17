package com.ader.backend.rest;

import com.ader.backend.rest.dto.FileDto;
import com.ader.backend.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/file")
@Slf4j
@RequiredArgsConstructor
public class FileController {

  private final FileService fileService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<FileDto>> getAll() {
    log.info("Requested all files");
    return ResponseEntity.ok(FileDto.toDto(fileService.getAllFiles()));
  }

  @GetMapping("offer/{id}")
  public ResponseEntity<List<FileDto>> getAllForOffer(@PathVariable Long id) {
    log.info("Requested all files for offer with id: [{}]", id);
    return ResponseEntity.ok(FileDto.toDto(fileService.getAllFilesForOffer(id)));
  }

  @GetMapping("user/{email}")
  public ResponseEntity<List<FileDto>> getAllForUser(
          @PathVariable String email,
          @RequestParam("offerIsNull") String offerIsNull
  ) {
    log.info("Requested all files for user with email: [{}]", email);
    return ResponseEntity.ok(FileDto.toDto(fileService.getAllFilesForUser(email, offerIsNull)));
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("upload")
  public ResponseEntity<FileDto> uploadFile(
          @RequestParam(required = false) Long offerId,
          @RequestParam("file") MultipartFile file) throws IOException {
    log.info("Requested uploading new file with size: [{}]", file.getBytes().length);
    return ResponseEntity.ok(FileDto.toDto(fileService.uploadFile(file, offerId)));
  }

  @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
  @DeleteMapping("{uuid}")
  public ResponseEntity deleteFile(@PathVariable UUID uuid) {
    log.info("Requested deleting file with UUID: [{}]", uuid);
    fileService.deleteFile(uuid);
    return ResponseEntity.ok().build();
  }
}
