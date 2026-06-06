package com.poupa.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Entidade usada no caminho de handoff para duvidas.
 *
 * Quando o cliente escolhe "tirar duvidas", o bot salva a mensagem aqui,
 * aplica a etiqueta visual na Meta e desativa o bot para atendimento humano.
 */
@Entity
@Table(name = "tb_duvidas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Duvida {

    /*
     * Chave primaria da duvida.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /*
     * Lead que enviou a duvida.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    /*
     * Profissional que devera responder a duvida.
     * Este campo tambem facilita consultas diretas por profissional.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private Profissional profissional;

    /*
     * Texto enviado pelo cliente.
     * columnDefinition="text" combina com o tipo TEXT da migration.
     */
    @Column(name = "mensagem_corpo", nullable = false, columnDefinition = "text")
    private String mensagemCorpo;

    /*
     * Data de criacao controlada pelo banco.
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
