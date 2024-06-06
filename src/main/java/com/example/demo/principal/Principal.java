package com.example.demo.principal;

import com.example.demo.model.Artista;
import com.example.demo.model.Musica;
import com.example.demo.model.TipoArtista;
import com.example.demo.repository.ArtistaRepository;
import com.example.demo.service.ConsultaChatGPT;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final ArtistaRepository repositorio;
    private Scanner leitura = new Scanner(System.in);

    public Principal(ArtistaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;

        while (opcao!= 9) {
            var menu = """
                    *** Screen Sound Músicas ***                    
                                        
                    1- Cadastrar artistas
                    2- Cadastrar músicas
                    3- Listar músicas
                    4- Buscar músicas por artistas
                    5- Pesquisar dados sobre um artista
                                    
                    9 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtistas();
                    break;
                case 2:
                    cadastrarMusicas();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4:
                    buscarMusicasPorArtista();
                    break;
                case 5:
                    pesquisarDadosDoArtista();
                    break;
                case 9:
                    System.out.println("Encerrando a aplicação!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void pesquisarDadosDoArtista() {
//        System.out.println("Pesquisar dados sobre qual artista? ");
//        var nome = leitura.nextLine();
//        var resposta = ConsultaChatGPT.obterInformacao(nome);
//        System.out.println(resposta.trim());
    }

    private void buscarMusicasPorArtista() {
        System.out.println("Digite o nome do artista: ");
        var nomeArtista = leitura.nextLine();
        List<Musica> musicas = this.repositorio.buscaMusicaPorArtista(nomeArtista);
        musicas.forEach(musica -> System.out.println(musica.getTitulo()));
        
    }

    private void listarMusicas() {
        List<Artista> artistas = repositorio.findAll();
        artistas.forEach(a -> a.getMusicas().forEach(System.out::println));
    }

    private void cadastrarMusicas() {
        System.out.println("Cadastrar muúsica de qual artista?");
        var nome = leitura.nextLine();
        Optional<Artista> artista = this.repositorio.findByNomeContainingIgnoreCase(nome);
        if (artista.isPresent()) {
            System.out.println("Digite o título da música: ");
            var nomeMusica = leitura.nextLine();
            Musica musica = new Musica(nomeMusica);
            musica.setArtista(artista.get());
            artista.get().getMusicas().add(musica);
            repositorio.save(artista.get());
        } else {
            System.out.println("Artista não encontrado!");
        }
    }

    private void cadastrarArtistas() {
        var cadastrarNovo = "s";

        while (cadastrarNovo.equalsIgnoreCase("s")) {
            System.out.println("Digite o nome do artista: ");
            var nome = leitura.nextLine();
            System.out.println("Digite o tipo do artista: (solo, banda, dupla)");
            var tipo = leitura.nextLine();
            TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());
            Artista artista = new Artista(nome, tipoArtista);
            this.repositorio.save(artista);
            System.out.println("Cadastrar novo artista? (s/n)");
            cadastrarNovo = leitura.nextLine();
        }
    }
}
