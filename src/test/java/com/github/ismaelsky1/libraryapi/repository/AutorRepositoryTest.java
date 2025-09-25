
package com.github.ismaelsky1.libraryapi.repository;

import com.github.ismaelsky1.libraryapi.model.Autor;
import com.github.ismaelsky1.libraryapi.model.GeneroLivro;
import com.github.ismaelsky1.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Test
    public void salvarTest() {
        Autor autor = new Autor();
        autor.setNome("Machado de Assis");
        autor.setDataNascimento(LocalDate.of(1839, 6, 21));
        autor.setNacionalidade("Brasileiro");

        Autor salvo = autorRepository.save(autor);

        Optional<Autor> encontrado = autorRepository.findById(salvo.getId());
        System.out.println(encontrado);
    }

    @Test
    public void atualizarTest() {
        Autor autor = new Autor();
        autor.setNome("Machado de Assis");
        autor.setDataNascimento(LocalDate.of(1839, 6, 21));
        autor.setNacionalidade("Brasileiro");

        Autor salvo = autorRepository.save(autor);

        salvo.setNome("Machado de Assis Atualizado");
        Autor atualizado = autorRepository.save(salvo);

        Optional<Autor> encontrado = autorRepository.findById(atualizado.getId());
        assert encontrado.isPresent();
        assert "Machado de Assis Atualizado".equals(encontrado.get().getNome());
    }

    @Test
    public void listarTest() {
        var autores = autorRepository.findAll();
        assert !autores.isEmpty() : "Nenhum autor cadastrado no banco.";
        autores.forEach(a -> System.out.println(a.getNome()));
    }

    @Test
    public void contarRegistrosTest() {
        long total = autorRepository.count();
        System.out.println("Total de autores: " + total);
        assert total >= 0 : "O número de registros não pode ser negativo.";
    }

    @Test
    public void deletarTest() {
        Autor autor = new Autor();
        autor.setNome("Autor para deletar");
        autor.setDataNascimento(LocalDate.of(1900, 1, 1));
        autor.setNacionalidade("Teste");

        Autor salvo = autorRepository.save(autor);

        autorRepository.deleteById(salvo.getId());

        Optional<Autor> encontrado = autorRepository.findById(salvo.getId());
        assert encontrado.isEmpty() : "O autor não foi deletado corretamente.";
    }

    @Test
    @Transactional
    public void salvaAutorComLivroTest() {
        Autor autor = new Autor();
        autor.setNome("J.K. Rowling");
        autor.setDataNascimento(LocalDate.of(1965, 7, 31));
        autor.setNacionalidade("Britânica");

        Livro livro1 = new Livro();
        livro1.setIsbn("978-3-16-148410-0");
        livro1.setTitulo("Harry Potter e a Pedra Filosofal");
        livro1.setDataPublicacao(LocalDate.of(1997, 6, 26));
        livro1.setPreco(new java.math.BigDecimal("39.90"));
        livro1.setAutor(autor);
        livro1.setGenero(GeneroLivro.FANTASIA);

        Livro livro2 = new Livro();
        livro2.setIsbn("978-3-16-148410-0");
        livro2.setTitulo("Harry Potter e a Câmara Secreta");
        livro2.setDataPublicacao(LocalDate.of(1997, 6, 26));
        livro2.setPreco(new java.math.BigDecimal("39.90"));
        livro2.setAutor(autor);
        livro2.setGenero(GeneroLivro.FANTASIA);

        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro1);
        autor.getLivros().add(livro2);

        autorRepository.save(autor);
//        livroRepository.saveAll(autor.getLivros());

        Optional<Autor> encontrado = autorRepository.findById(autor.getId());
        assert encontrado.isPresent() : "Autor não encontrado.";
        assert encontrado.get().getLivros() != null && !encontrado.get().getLivros().isEmpty() : "Livros do autor não foram salvos corretamente.";

        System.out.println("Autor e livros salvos com sucesso: " + encontrado.get().getNome() + ", Livros: " + encontrado.get().getLivros().size() + " - Nomes: " + encontrado.get().getLivros().stream().map(Livro::getTitulo).toList());


    }
}