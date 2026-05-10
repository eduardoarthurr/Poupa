package com.poupa.api.entity;

import com.poupa.api.entity.enums.CategoriaProfissional;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "tb_professionals")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Profissional {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private CategoriaProfissional categoria;

    @Column(name = "whatsapp_oficial", unique = true)
    private String whatsappOficial;

    @Column(name = "expediente_inicio")
    private LocalTime expedienteInicio;

    @Column(name = "expediente_fim")
    private LocalTime expedienteFim;

    @Column(name = "google_calendar_id")
    private String googleCalendarId;
}
