package org.example.service;

import org.example.dto.CartorioRequest;
import org.example.model.Atribuicao;
import org.example.model.Cartorio;
import org.example.repository.AtribuicaoRepository;
import org.example.repository.CartorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartorioService {

    @Autowired
    private CartorioRepository cartorioRepository;

    @Autowired
    private AtribuicaoRepository atribuicaoRepository;

    public Cartorio cadastrarCartorio(Cartorio cartorio) {
        return cartorioRepository.save(cartorio);
    }

    public Cartorio cadastrarCartorio(Cartorio cartorio, List<Atribuicao> atribuicoes) {
        Cartorio savedCartorio = cartorioRepository.save(cartorio);


        for (Atribuicao atribuicao : atribuicoes) {
            atribuicao.setId(savedCartorio.getId().toString());
            atribuicaoRepository.save(atribuicao);
        }

        return savedCartorio;
    }

    public class CartorioExistenteException extends RuntimeException {
        public CartorioExistenteException(String message) {
            super(message);
        }
    }

    public Cartorio atualizarCartorio(Long id, Cartorio cartorioAtualizado,List<Atribuicao> novasAtribuicoes) {
        Cartorio cartorioExistente = cartorioRepository.findById(id);
        cartorioExistente.setNome(cartorioAtualizado.getNome());

        for (Atribuicao atribuicao : novasAtribuicoes) {

            if (atribuicao.getCodigo() != null) {
                Atribuicao atribuicaoExistente = atribuicaoRepository.findByCodigo(atribuicao.getCodigo())
                        .orElseThrow(() -> new RuntimeException("Atribuição não encontrada"));
                atribuicaoExistente.setNome(atribuicao.getNome());
                atribuicaoExistente.setSituacao(atribuicao.isSituacao());
                atribuicaoRepository.save(atribuicaoExistente);
            } else {
                atribuicaoRepository.save(atribuicao);
            }
        }

        // Salva o cartório atualizado
        return cartorioRepository.save(cartorioExistente);
    }

    public CartorioRequest helloWorld(CartorioRequest request) {

        return request; // Retorna o mesmo objeto recebido
    }
    public Cartorio convertToCartorio(CartorioRequest request) {
        // Implemente a lógica de conversão conforme sua estrutura
        Cartorio cartorio = new Cartorio();
        cartorio.setNome(request.getNome());
        cartorio.setObservacao(request.getObservacao());
        cartorio.setAtribuicaoId(request.getAtribuicaoId());
        cartorio.setSituacaoId(request.getSituacaoId());
        // Adicione outros campos conforme necessário
        return cartorio;
    }

}
