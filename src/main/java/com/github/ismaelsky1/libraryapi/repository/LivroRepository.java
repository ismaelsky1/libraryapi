package com.github.ismaelsky1.libraryapi.repository;

import com.github.ismaelsky1.libraryapi.model.Autor;
import com.github.ismaelsky1.libraryapi.model.GeneroLivro;
import com.github.ismaelsky1.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    // query Method

    List<Livro> findByAutor(Autor autor);

    List<Livro> findByTitulo(String titulo);

    List<Livro> findByTituloContaining(String titulo);

    List<Livro> findByIsbn(String isbn);

    List<Livro> findByTituloAndPreco(String titulo, BigDecimal Preco);

    List<Livro> findByTituloOrIsbn(String titulo, String isbn);

    //JPQL
    @Query("select l from Livro l order by l.titulo")
    List<Livro> listTodos();

    @Query("select a from Livro l join l.autor a")
    List<Livro> listAutoresDosLivros();

    @Query("""
        select l.genero
        from Livro l
        join l.autor a
        where a.nacionalidade = 'Brasileiro'
        order by l.genero
    """)
    List<String> listGenerosAutoresBrasileiros();


    //namad parameter
    @Query("""
        select l
        from Livro l
        where l.genero = :genero
        order by :paramOrdenacao
    """)
    List<Livro> findByGenero(@Param("genero") GeneroLivro generoLivro, @Param("paramOrdenacao") String nomePropriedade);

    //positional parameter
    @Query("""
        select l
        from Livro l
        where l.genero = ?1
        order by ?2
    """)
    List<Livro> findByGeneroPositional(GeneroLivro generoLivro, String nomePropriedade);


    @Modifying
    @Transactional
    @Query("""
        delete
        from Livro l
        where l.genero = ?1
    """)
    void deleteByGenero(GeneroLivro generoLivro);

    @Modifying
    @Transactional
    @Query("""
        update Livro l set dataPublicacao = ?1
    """)
    void updateDataPublicacao(LocalDate dataPublicacao );








}
