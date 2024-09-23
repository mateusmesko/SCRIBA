package org.example.dto;

import lombok.Data;
import javax.persistence.Id;

@Data
public class SituacaoDTO {
    @Id
    private String id;
    private String nome;
}
