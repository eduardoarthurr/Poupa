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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Entidade do catalogo de servicos do profissional.
 *
 * Exemplos: corte masculino, barba, tranca nagô, manutencao etc.
 * O campo duracaoMinutos e essencial porque o motor de disponibilidade usa essa
 * duracao para calcular quais horarios cabem na agenda.
 */
@Entity
@Table(name = "tb_servicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Servico {

    /*
     * Chave primaria do servico.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /*
     * Profissional dono do servico.
     * Cada profissional tem seu proprio catalogo e seus proprios precos.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private Profissional profissional;

    /*
     * Nome exibido para o cliente e para o dashboard.
     */
    @Column(nullable = false, length = 100)
    private String nome;

    /*
     * Agrupador opcional para organizacao e IA.
     * Exemplo: Corte, Barba, Tranca, Manutencao.
     */
    @Column(length = 50)
    private String categoria;

    /*
     * Preco atual do servico.
     * BigDecimal e usado para dinheiro porque evita imprecisoes de double/float.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    /*
     * Duracao do servico em minutos.
     * O AvailabilityEngine usa esse valor para calcular data_hora_fim.
     */
    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracaoMinutos;

    /*
     * Indica se o servico aparece ou nao no fluxo de agendamento.
     */
    @Column
    private Boolean active = true;

    /*
     * Data de criacao controlada pelo banco.
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
