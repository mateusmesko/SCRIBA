package org.example.controller;

import org.example.dto.CartorioDTO;
import org.example.dto.CartorioRequest;
import org.example.model.Atribuicao;
import org.example.model.Cartorio;
import org.example.repository.AtribuicaoRepository;
import org.example.repository.CartorioRepository;
import org.example.service.CartorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cartorios")
public class CartorioController {

    @Autowired
    private CartorioService cartorioService;
    @Autowired
    private CartorioRepository cartorioRepository;
    @Autowired
    private CartorioRepository situacaoRepository;

    @Autowired
    private AtribuicaoRepository atribuicaoRepository;

    @PostMapping
    public ResponseEntity<?> createCartorio(@RequestBody CartorioRequest cartorioRequest) {
        // Converte o CartorioRequest em Cartorio
        Cartorio cartorio = cartorioService.convertToCartorio(cartorioRequest);
        if (cartorioRepository.existsByNome(cartorio.getNome())) {
            // Aqui você deve buscar o cartório existente
            Cartorio cartorioExistente = (Cartorio) cartorioRepository.findByNome(cartorio.getNome())
                    .orElseThrow(() -> new RuntimeException("Cartório não encontrado, mas deveria existir"));
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nome existente no registro com código " + cartorioExistente.getId());
        }


        List<Atribuicao> atribuicoes = cartorioRequest.getAtribuicoes();
        cartorioService.cadastrarCartorio(cartorio, atribuicoes);

        // Retorna as atribuições como resposta
        return ResponseEntity.status(HttpStatus.CREATED).body(cartorioRequest);
        // Salva o cartório e as atribuições

    }

    @PutMapping("/{id}")
    public  ResponseEntity<?> atualizarCartorio(@PathVariable Long id, @RequestBody CartorioRequest request) {
        Cartorio cartorio = cartorioService.convertToCartorio(request);
        if (cartorioRepository.existsByNome(cartorio.getNome())) {
            // Aqui você deve buscar o cartório existente
            Cartorio cartorioExistente = (Cartorio) cartorioRepository.findByNome(cartorio.getNome())
                    .orElseThrow(() -> new RuntimeException("Cartório não encontrado, mas deveria existir"));
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nome existente no registro com código " + cartorioExistente.getId());
        }
        // Salva o cartório e as atribuições
        List<Atribuicao> atribuicoes = request.getAtribuicoes();

        Cartorio savedCartorio = cartorioService.atualizarCartorio(id, cartorio, atribuicoes);
        return ResponseEntity.ok(atribuicoes);
    }

    @GetMapping
    public ResponseEntity<Page<CartorioDTO>> listarCartoriosPaginados(Pageable pageable) {
        // Busca a página de Cartorios
        Page<Cartorio> cartoriosPage = cartorioRepository.findAll(pageable);

        // Converte a página de Cartorios para CartorioDTO
        Page<CartorioDTO> cartoriosDtoPage = cartoriosPage.map(cartorio ->
                new CartorioDTO(cartorio.getId(), cartorio.getNome())
        );

        return ResponseEntity.ok(cartoriosDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCartorioPorId(@PathVariable Long id) {
        // Busca o cartório pelo ID, se não encontrar, lança exceção
        Cartorio cartorio = cartorioRepository.findById(id);

        if (cartorio == null) {
            return ResponseEntity.notFound().build(); // Retorna 404 se o cartório não existir
        }

        List<Atribuicao> atribuicoesArray = new ArrayList<>();
        CartorioRequest cartorioRequest = new CartorioRequest(); // Inicializa o CartorioRequest
        cartorioRequest.setId(cartorio.getId());
        cartorioRequest.setNome(cartorio.getNome());
        cartorioRequest.setObservacao(cartorio.getObservacao());
        cartorioRequest.setSituacaoId(cartorio.getSituacaoId());

        List<Atribuicao> atribuicoes = atribuicaoRepository.findAll();
        for (Atribuicao atribuicao : atribuicoes) {
            if (atribuicao != null && cartorio != null) {
                String atribuicaoId = String.valueOf(atribuicao.getId());
                String cartorioId = String.valueOf(cartorio.getId());
                if (atribuicaoId.equals(cartorioId)) {
                    atribuicoesArray.add(atribuicao);
                }
            }
        }
        cartorioRequest.setAtribuicoes(atribuicoesArray);
        // Retorna todos os dados do cartório
        return ResponseEntity.ok(cartorioRequest);
    }

    @DeleteMapping("/{id}")
    @Transactional // Adiciona esta anotação
    public ResponseEntity<String> excluirCartorio(@PathVariable Long id) {
        if (cartorioRepository.existsById(id)) {
            cartorioRepository.deleteById(id);
            return ResponseEntity.ok("Cartório deletado com sucesso.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
