package com.poupa.api.entity;

import com.poupa.api.entity.enums.ConversationState;
import io.hypersistence.utils.hibernate.type.json.JsonType;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * Entidade que representa um cliente em conversa com um profissional.
 *
 * No Poupa, "Lead" nao e apenas um cadastro simples: ele tambem guarda o estado
 * atual da conversa do bot. Isso permite que cada mensagem recebida pelo webhook
 * continue exatamente de onde a conversa anterior parou.
 */
@Entity
@Table(
        name = "tb_leads",
        /*
         * Regra multi-tenant:
         * o mesmo telefone pode falar com profissionais diferentes.
         * Por isso a unicidade correta e professional_id + telefone_whatsapp,
         * e nao telefone_whatsapp sozinho.
         */
        uniqueConstraints = @UniqueConstraint(
                name = "uk_leads_professional_phone",
                columnNames = {"professional_id", "telefone_whatsapp"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lead {

    /*
     * Chave primaria UUID.
     * Usamos UUID para evitar IDs sequenciais previsiveis e facilitar futuras
     * integracoes distribuidas.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /*
     * Muitos leads pertencem a um profissional.
     *
     * @ManyToOne cria a relacao N:1 no Java.
     * @JoinColumn informa qual coluna da tabela tb_leads guarda a FK.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private Profissional profissional;

    /*
     * Telefone WhatsApp do cliente final, nao do profissional.
     * Esse campo sera usado para localizar a conversa quando novas mensagens chegarem.
     */
    @Column(name = "telefone_whatsapp", nullable = false, length = 20)
    private String telefoneWhatsapp;

    /*
     * Kill-switch do bot.
     * true: a maquina de estados pode responder automaticamente.
     * false: o profissional assumiu manualmente e o bot deve ficar quieto.
     */
    @Column(name = "is_bot_active")
    private Boolean botActive = true;

    /*
     * Estado atual da conversa.
     *
     * EnumType.STRING salva "START", "SELECTING_INTENT" etc no banco.
     * Isso e melhor que salvar numeros, porque fica legivel e evita bugs se a ordem
     * do enum mudar no futuro.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "current_state", length = 50)
    private ConversationState currentState = ConversationState.START;

    /*
     * Origem de marketing ou aquisicao do lead.
     * Exemplo: instagram, google, indicacao.
     */
    @Column(length = 50)
    private String origem;

    /*
     * Contexto temporario da conversa em JSONB.
     *
     * Aqui entram dados que mudam durante o fluxo: servico escolhido, slot escolhido,
     * contador de fotos, URLs de midia pendentes etc.
     *
     * @Type(JsonType.class) vem do Hypersistence Utils e ensina o Hibernate a mapear
     * Map<String, Object> para JSONB do PostgreSQL.
     */
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> context = new HashMap<>();

    /*
     * Data da ultima interacao.
     * insertable=false deixa o banco preencher o valor padrao definido na migration.
     */
    @Column(name = "last_interaction", insertable = false)
    private LocalDateTime lastInteraction;

    /*
     * Data de criacao do lead.
     * insertable=false e updatable=false indicam que a aplicacao nao deve escrever
     * nessa coluna; o PostgreSQL controla o DEFAULT CURRENT_TIMESTAMP.
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
