package com.github.ismaelsky1.libraryapi.repository;

import com.github.ismaelsky1.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
}
