package org.example.service;

import org.example.dto.SituacaoDTO;
import org.example.model.Situacao;
import org.example.repository.SituacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SituacaoService {

    @Autowired
    private SituacaoRepository situacaoRepository;

    // Criar nova situação
    public Situacao criarSituacao(Situacao situacao) {
        return situacaoRepository.save(situacao);
    }

    // Listar todas as situações
    public List<SituacaoDTO> listarTodasSituacoes() {
        return situacaoRepository.findAll()
                .stream()
                .map(s -> {
                    SituacaoDTO dto = new SituacaoDTO();
                    dto.setId(s.getId());
                    dto.setNome(s.getNome());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Buscar situação por ID
    public Optional<SituacaoDTO> buscarSituacaoPorId(String id) {
        return situacaoRepository.findById(id)
                .map(s -> {
                    SituacaoDTO dto = new SituacaoDTO();
                    dto.setId(s.getId());
                    dto.setNome(s.getNome());
                    return dto;
                });
    }

    // Atualizar situação
    public Optional<Situacao> atualizarSituacao(String id, Situacao situacao) {
        return situacaoRepository.findById(id)
                .map(s -> {
                    s.setNome(situacao.getNome());
                    return situacaoRepository.save(s);
                });
    }

    // Excluir situação
    public void excluirSituacao(String id) {
        situacaoRepository.deleteById(id);
    }

}
