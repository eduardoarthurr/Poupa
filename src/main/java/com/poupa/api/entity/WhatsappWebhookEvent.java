package com.poupa.api.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
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
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * Entidade de auditoria/idempotencia para eventos recebidos da Meta.
 *
 * Webhooks podem ser reenviados quando a Meta nao recebe resposta 200 a tempo.
 * Ao salvar o whatsappMessageId como unico, o Poupa consegue evitar responder
 * duas vezes a mesma mensagem.
 */
@Entity
@Table(name = "tb_whatsapp_webhook_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WhatsappWebhookEvent {

    /*
     * Chave primaria do evento recebido.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /*
     * Profissional associado ao evento.
     * Pode ficar nulo se o evento chegou antes de conseguirmos rotear o phoneNumberId.
     */
    @ManyToOne
    @JoinColumn(name = "professional_id")
    private Profissional profissional;

    /*
     * ID tecnico do numero que recebeu o evento na Cloud API.
     * E a partir dele que localizamos a WhatsappIntegration e o profissional.
     */
    @Column(name = "phone_number_id", nullable = false, length = 100)
    private String phoneNumberId;

    /*
     * ID unico da mensagem/evento no WhatsApp.
     * A constraint unique no banco protege contra processamento duplicado.
     */
    @Column(name = "whatsapp_message_id", nullable = false, unique = true, length = 255)
    private String whatsappMessageId;

    /*
     * Tipo do evento recebido.
     * Exemplo futuro: messages, statuses, errors.
     */
    @Column(name = "event_type", nullable = false, length = 80)
    private String eventType;

    /*
     * Payload completo recebido no webhook.
     * Guardar JSONB ajuda em auditoria e debug sem precisar criar coluna para cada
     * detalhe da Meta logo no MVP.
     */
    @Type(JsonType.class)
    @Column(nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> payload = new HashMap<>();

    /*
     * Momento em que o Poupa terminou de processar o evento.
     * Se estiver nulo, significa recebido mas ainda nao processado.
     */
    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    /*
     * Data de recebimento controlada pelo banco.
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
