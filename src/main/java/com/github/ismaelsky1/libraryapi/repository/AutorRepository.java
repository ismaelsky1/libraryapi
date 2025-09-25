package com.github.ismaelsky1.libraryapi.repository;

import com.github.ismaelsky1.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
}
