package com.ader.backend.repository;

import com.ader.backend.entity.persona.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    @Query("SELECT persona " +
            "FROM Persona persona " +
            "JOIN User user ON user = persona.user " +
            "WHERE user.email = ?1")
    List<Persona> findAllByUserEmail(String userEmail);
}
