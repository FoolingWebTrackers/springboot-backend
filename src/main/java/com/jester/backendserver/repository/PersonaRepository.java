package com.jester.backendserver.repository;

import com.jester.backendserver.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Persona getPersonaById(Integer id);
}
