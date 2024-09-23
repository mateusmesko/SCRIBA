package org.example.controller;

import org.example.dto.SituacaoDTO;
import org.example.model.Cartorio;
import org.example.model.Situacao;
import org.example.repository.CartorioRepository;
import org.example.repository.SituacaoRepository;
import org.example.service.SituacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/situacoes")
public class SituacaoController {

    @Autowired
    private SituacaoService situacaoService;
    @Autowired
    private SituacaoRepository situacaoRepository;
    @Autowired
    private CartorioRepository cartorioRepository;

    @PostMapping
    public ResponseEntity<?> criarSituacao(@RequestBody Situacao situacao) {
        // Verifica se o cartório já existe antes de tentar criar
        if (situacaoRepository.existsById(situacao.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Registro já cadastrado.");
        }

        // Cria uma nova situação
        Situacao novaSituacao = situacaoService.criarSituacao(situacao);

        // Retorna o objeto criado com status 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(novaSituacao);
    }

    // Rota para listar todas as situações
    @GetMapping
    public ResponseEntity<List<SituacaoDTO>> listarTodasSituacoes() {
        List<SituacaoDTO> situacoes = situacaoService.listarTodasSituacoes();
        return ResponseEntity.ok(situacoes);
    }

    // Rota para buscar situação por ID
    @GetMapping("/{id}")
    public ResponseEntity<SituacaoDTO> buscarSituacaoPorId(@PathVariable String id) {
        Optional<SituacaoDTO> situacao = situacaoService.buscarSituacaoPorId(id);
        return situacao.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Rota para atualizar uma situação
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarSituacao(@PathVariable String id, @RequestBody Situacao situacao) {
        Optional<Situacao> situacaoAtualizada = situacaoService.atualizarSituacao(id, situacao);

        if (situacaoRepository.existsById(situacao.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Situação já existente.");
        }
        return situacaoAtualizada.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Rota para excluir uma situação
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirSituacao(@PathVariable String id) {
        List<Cartorio> cartorios = cartorioRepository.findAll();
        boolean situacaoVinculada = cartorios.stream()
                .anyMatch(cartorio -> cartorio.getSituacaoId().equals(id));

        if (situacaoVinculada) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Registro utilizado em outro cadastro");
        }
        situacaoService.excluirSituacao(id);
        return ResponseEntity.noContent().build();
    }
}
