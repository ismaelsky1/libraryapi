package com.github.ismaelsky1.libraryapi.repository;

import com.github.ismaelsky1.libraryapi.model.Autor;
import com.github.ismaelsky1.libraryapi.model.GeneroLivro;
import com.github.ismaelsky1.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    LivroRepository repository;
    @Autowired
    AutorRepository autorRepository;

    private Autor criarAutor() {
        Autor autor = new Autor();
        autor.setNome("Autor Teste");
        autor.setDataNascimento(LocalDate.of(1980, 1, 1));
        autor.setNacionalidade("Brasileiro");
        return autorRepository.save(autor);
    }

    private Livro criarLivro(Autor autor) {
        Livro livro = new Livro();
        livro.setIsbn("1234567890");
        livro.setTitulo("Livro Teste");
        livro.setDataPublicacao(LocalDate.of(2020, 1, 1));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setPreco(new BigDecimal("49.90"));
        livro.setAutor(autor);
        return livro;
    }

    @Test
    @Transactional
    void criarLivroTest() {
        Autor autor = criarAutor();
        Livro livro = criarLivro(autor);
        Livro salvo = repository.save(livro);
        assert salvo.getId() != null;
    }

    @Test
    @Transactional
    void criarLivroTestCascat() {
        Autor autor = new Autor();
        autor.setNome("Autor Teste");
        autor.setDataNascimento(LocalDate.of(1980, 1, 1));
        autor.setNacionalidade("Brasileiro");

        Livro livro = criarLivro(autor);
        Livro salvo = repository.save(livro);
        assert salvo.getId() != null;
        System.out.println("Name do Autor and Livro: " + salvo.getAutor().getNome() + " - " + salvo.getTitulo());
    }

    @Test
    @Transactional
    void lerLivroTest() {
        Autor autor = criarAutor();
        Livro livro = criarLivro(autor);
        Livro salvo = repository.save(livro);
        Optional<Livro> encontrado = repository.findById(salvo.getId());
        assert encontrado.isPresent();
        assert "Livro Teste".equals(encontrado.get().getTitulo());
    }

    @Test
    @Transactional
    void atualizarLivroTest() {
        Autor autor = criarAutor();
        Livro livro = criarLivro(autor);
        Livro salvo = repository.save(livro);
        salvo.setTitulo("Livro Atualizado");
        repository.save(salvo);
        Optional<Livro> atualizado = repository.findById(salvo.getId());
        assert atualizado.isPresent();
        assert "Livro Atualizado".equals(atualizado.get().getTitulo());
    }

    @Test
    @Transactional
    void deletarLivroTest() {
        Autor autor = criarAutor();
        Livro livro = criarLivro(autor);
        Livro salvo = repository.save(livro);
        repository.deleteById(salvo.getId());
        Optional<Livro> encontrado = repository.findById(salvo.getId());
        assert encontrado.isEmpty();
    }

    @Test
    @Transactional
    void autalizarAutorDoLivroTest() {
        Autor autorOriginal = criarAutor();
        Livro livro = criarLivro(autorOriginal);
        Livro salvo = repository.save(livro);

        // Buscar um autor já existente no banco
        Autor autorExistente = autorRepository.findAll().stream().findFirst().orElseGet(this::criarAutor);

        salvo.setAutor(autorExistente);
        repository.save(salvo);

        Optional<Livro> atualizado = repository.findById(salvo.getId());
        assert atualizado.isPresent();
        assert autorExistente.getNome().equals(atualizado.get().getAutor().getNome());
    }

    @Test
    void pesquisaPorTituloTest() {
        Autor autor = criarAutor();
        Livro livro = criarLivro(autor);
        repository.save(livro);

        var livrosEncontrados = repository.findByTitulo("Livro Teste");
        assert !livrosEncontrados.isEmpty();
        livrosEncontrados.forEach(System.out::println);
    }

    @Test
    void listarLivrosComQueryJPQLTest() {
        Autor autor = criarAutor();
        Livro livro = criarLivro(autor);
        repository.save(livro);

        var livrosEncontrados = repository.listTodos();
        assert !livrosEncontrados.isEmpty();
        livrosEncontrados.forEach(System.out::println);
    }

    @Test
    void listAutoresDosLivrosTest() {
        Autor autor = criarAutor();
        Livro livro = criarLivro(autor);
        repository.save(livro);

        var listAutoresDosLivrosTest = repository.listAutoresDosLivros();
        assert !listAutoresDosLivrosTest.isEmpty();
        listAutoresDosLivrosTest.forEach(System.out::println);
    }

    @Test
    void listaGenerosAutoresBrasileiros() {
        Autor autor = criarAutor();
        Livro livro = criarLivro(autor);
        repository.save(livro);

        var resultado = repository.listGenerosAutoresBrasileiros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listaPorgeneroQueryParamTest() {
        Autor autor = criarAutor();
        Livro livro = criarLivro(autor);
        repository.save(livro);

        var resultado = repository.findByGenero(GeneroLivro.FANTASIA, "preco");
        resultado.forEach(System.out::println);
    }

    @Test
    void deleteByGeneroTest() {
        Autor autor = criarAutor();
        Livro livro = criarLivro(autor);
        repository.save(livro);

        repository.deleteByGenero(GeneroLivro.FICCAO);
    }
}

