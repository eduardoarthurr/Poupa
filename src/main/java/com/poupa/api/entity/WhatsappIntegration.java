package com.poupa.api.entity;

import com.poupa.api.entity.enums.WhatsappIntegrationProvider;
import com.poupa.api.entity.enums.WhatsappIntegrationStatus;
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

import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Entidade que guarda a conexao oficial do profissional com a WhatsApp Cloud API.
 *
 * Ela existe para separar o cadastro do profissional dos dados tecnicos da Meta.
 * Assim o Poupa pode gerenciar varios profissionais, cada um com seu proprio
 * phone_number_id e WABA.
 */
@Entity
@Table(
        name = "tb_whatsapp_integrations",
        /*
         * Nesta fase, cada profissional tera uma integracao ativa por provider.
         * Se no futuro suportarmos outro provedor, como outro canal oficial,
         * esse par professional_id + provider continua controlando duplicidade.
         */
        uniqueConstraints = @UniqueConstraint(
                name = "uk_whatsapp_integrations_professional_provider",
                columnNames = {"professional_id", "provider"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WhatsappIntegration {

    /*
     * Chave primaria da integracao.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /*
     * Profissional dono desta integracao.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private Profissional profissional;

    /*
     * Provedor da integracao.
     * Hoje usamos apenas META_CLOUD_API, mas o enum deixa o desenho aberto para
     * crescimento controlado.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WhatsappIntegrationProvider provider = WhatsappIntegrationProvider.META_CLOUD_API;

    /*
     * ID do Business Portfolio/Meta Business relacionado ao onboarding.
     */
    @Column(name = "meta_business_id", length = 100)
    private String metaBusinessId;

    /*
     * ID da WhatsApp Business Account.
     * WABA e a conta de WhatsApp Business Platform onde o numero fica vinculado.
     */
    @Column(name = "waba_id", nullable = false, length = 100)
    private String wabaId;

    /*
     * ID tecnico do numero na Cloud API.
     * Esse campo e essencial para rotear webhooks: a Meta informa o phone_number_id
     * que recebeu a mensagem, e o Poupa encontra o profissional por ele.
     */
    @Column(name = "phone_number_id", nullable = false, unique = true, length = 100)
    private String phoneNumberId;

    /*
     * Numero legivel para exibicao, por exemplo +55 11 99999-9999.
     */
    @Column(name = "display_phone_number", length = 30)
    private String displayPhoneNumber;

    /*
     * Nome aprovado/exibido pela Meta para aquele numero.
     */
    @Column(name = "verified_name", length = 100)
    private String verifiedName;

    /*
     * Status interno da conexao.
     * Ajuda o dashboard a mostrar se o WhatsApp esta conectado, pendente ou com erro.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WhatsappIntegrationStatus status = WhatsappIntegrationStatus.PENDING;

    /*
     * Token de acesso da Meta.
     * O nome deixa claro que nunca devemos salvar token puro em producao.
     * Antes de persistir, o service responsavel deve criptografar esse valor.
     */
    @Column(name = "access_token_encrypted", columnDefinition = "text")
    private String accessTokenEncrypted;

    /*
     * Momento em que a conexao foi concluida com sucesso.
     */
    @Column(name = "connected_at")
    private LocalDateTime connectedAt;

    /*
     * Criado pelo banco.
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /*
     * Atualizado pelo banco ou por camada de service em atualizacoes futuras.
     * insertable=false evita que a aplicacao tente definir valor inicial manualmente.
     */
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;
}
