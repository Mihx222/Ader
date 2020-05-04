package com.ader.backend.repository;

import com.ader.backend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByOffer_Id(Long id);

    Optional<File> findByUuid(UUID uuid);

    List<File> findAllByUser_Email(String email);

    Optional<File> findByName(String name);

    void deleteByUuid(UUID uuid);

    @Query(nativeQuery = true,
            value = "select * from files f " +
                    "join users u on u.id = f.user_id " +
                    "where f.name = ?1 and " +
                    "f.type = ?2 and " +
                    "f.user_id = ?3")
    File findByNameAndTypeAndUserId(String fileName, String type, Long userId);

    @Query(nativeQuery = true,
            value = "select * from files f " +
                    "where f.user_id = ?1 and " +
                    "f.offer_id is null"
    )
    List<File> findAllFilesForUserWithoutOffer(Long userId);
}
