package com.poupa.api.entity;

import com.poupa.api.entity.enums.StatusAgendamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Entidade que representa um agendamento criado a partir da conversa.
 *
 * Ela liga tres partes importantes do dominio:
 * - Lead: quem esta agendando.
 * - Servico: o que sera feito.
 * - Profissional: quem vai atender.
 */
@Entity
@Table(name = "tb_agendamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    /*
     * Chave primaria do agendamento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /*
     * Cliente/lead dono do agendamento.
     * Muitos agendamentos podem pertencer ao mesmo lead ao longo do tempo.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    /*
     * Servico selecionado pelo cliente.
     * A duracao do servico sera usada pelo motor de disponibilidade.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Servico servico;

    /*
     * Profissional responsavel pelo atendimento.
     * Mesmo existindo caminho via lead.profissional e servico.profissional,
     * manter professional_id aqui facilita consultas de agenda e dashboard.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private Profissional profissional;

    /*
     * Inicio do atendimento.
     */
    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    /*
     * Fim do atendimento.
     * Esse valor e calculado por inicio + duracao_minutos do servico.
     */
    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    /*
     * Snapshot financeiro do preco no momento do agendamento.
     * Isso evita que uma mudanca futura no preco do servico altere faturamento passado.
     */
    @Column(name = "valor_pago", precision = 10, scale = 2)
    private BigDecimal valorPago;

    /*
     * Momento em que o humano confirmou o agendamento.
     */
    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;

    /*
     * Status operacional do agendamento.
     * Salvo como texto por EnumType.STRING para manter o banco legivel.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusAgendamento status = StatusAgendamento.EM_TRIAGEM;

    /*
     * URLs das midias enviadas pelo cliente.
     * PostgreSQL suporta array nativo text[].
     * @JdbcTypeCode(SqlTypes.ARRAY) orienta o Hibernate a mapear String[] para esse tipo.
     */
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "midia_urls", columnDefinition = "text[]")
    private String[] midiaUrls;

    /*
     * Data de criacao controlada pelo banco.
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
