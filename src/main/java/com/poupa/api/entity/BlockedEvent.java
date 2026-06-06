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
 * Entidade que representa bloqueios manuais na agenda.
 *
 * Exemplos: almoco, folga, consulta medica, feriado ou dia fechado.
 * O AvailabilityEngine deve remover qualquer slot que colida com estes periodos.
 */
@Entity
@Table(name = "tb_blocked_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockedEvent {

    /*
     * Chave primaria do bloqueio.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /*
     * Profissional dono do bloqueio.
     * Bloqueios nunca devem afetar a agenda de outro profissional.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private Profissional profissional;

    /*
     * Inicio do periodo indisponivel.
     */
    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    /*
     * Fim do periodo indisponivel.
     */
    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    /*
     * Descricao livre para o profissional entender o motivo do bloqueio.
     */
    @Column(length = 255)
    private String descricao;

    /*
     * Data de criacao controlada pelo banco.
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
