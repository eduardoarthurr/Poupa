package com.poupa.api.entity;

import com.poupa.api.entity.enums.CategoriaProfissional;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;



/*
 @Entity: Diz ao JPA/Hibernate que esta classe é uma entidade de banco de dados.
 Ela será mapeada para uma tabela.

 @Table(name = "tb_professionals"): Define o nome exato da tabela no PostgreSQL.
 Se não usarmos, o Java tentaria criar uma tabela chamada "Profissional".
 */


@Entity
@Table(name = "tb_professionals")

/*
ANOTAÇÕES LOMBOK:
 @Getter / @Setter: Criam automaticamente os métodos para ler e alterar os campos (evita centenas de linhas de código).
 @NoArgsConstructor: Cria o construtor vazio (Obrigatório para o Hibernate conseguir carregar os dados do banco).
 @AllArgsConstructor: Cria um construtor com todos os campos (Facilita criar o objeto em uma linha no código).
*/

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Profissional {


    /*
    @Id: indica que este campo representa  Chave Primária (Primary Key = PK da entidade).
    @GeneratedValue(strategy = GenerationType.UUID): Define que o ID não será 1, 2, 3...
    mas sim um código único (UUID) gerado automaticamente pelo Java antes de salvar.
    */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /*
     Sem anotação: O Java mapeia automaticamente para uma coluna de mesmo nome "nome".
     */
    private String nome;

    /*
    @Enumerated(EnumType.STRING): Crucial! Diz para salvar o texto do Enum ("BARBEIRO" ou "TRANCISTA") no banco.
    Sem isso, o banco salvaria números (0 ou 1), o que dificulta a leitura manual do banco.
    */

    @Enumerated(EnumType.STRING)
    private CategoriaProfissional categoria;


    /*
    @Column: Usada quando o nome no Java é diferente do nome no Banco ou possui regras.
    name = "whatsapp_oficial": Mapeia a variável whatsappOficial para a coluna whatsapp_oficial.
    unique = true: Garante que não existam dois profissionais com o mesmo número cadastrado.
    */

    @Column(name = "whatsapp_oficial", unique = true)
    private String whatsappOficial;

    /*
    LocalTime: Tipo do Java que guarda apenas a hora (HH:mm:ss).
    Perfeito para o campo TIME do PostgreSQL.
    */

    @Column(name = "expediente_inicio")
    private LocalTime expedienteInicio;

    @Column(name = "expediente_fim")
    private LocalTime expedienteFim;

    /*
    Campo para integração futura com Google Calendar.
    */

    @Column(name = "google_calendar_id")
    private String googleCalendarId;
}
