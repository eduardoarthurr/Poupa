CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE tb_professionals (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(20) NOT NULL CHECK (categoria IN ('BARBEIRO', 'TRANCISTA')),
    whatsapp_oficial VARCHAR(20) UNIQUE,
    expediente_inicio TIME NOT NULL,
    expediente_fim TIME NOT NULL,
    google_calendar_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_whatsapp_integrations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    professional_id UUID NOT NULL REFERENCES tb_professionals(id) ON DELETE CASCADE,
    provider VARCHAR(30) NOT NULL DEFAULT 'META_CLOUD_API'
        CHECK (provider IN ('META_CLOUD_API')),
    meta_business_id VARCHAR(100),
    waba_id VARCHAR(100) NOT NULL,
    phone_number_id VARCHAR(100) NOT NULL UNIQUE,
    display_phone_number VARCHAR(30),
    verified_name VARCHAR(100),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'CONNECTED', 'ERROR', 'DISCONNECTED')),
    access_token_encrypted TEXT,
    connected_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_whatsapp_integrations_professional_provider
        UNIQUE (professional_id, provider)
);

CREATE TABLE tb_servicos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    professional_id UUID NOT NULL REFERENCES tb_professionals(id) ON DELETE CASCADE,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    preco DECIMAL(10, 2) NOT NULL,
    duracao_minutos INTEGER NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_leads (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    professional_id UUID NOT NULL REFERENCES tb_professionals(id) ON DELETE CASCADE,
    telefone_whatsapp VARCHAR(20) NOT NULL,
    is_bot_active BOOLEAN DEFAULT TRUE,
    current_state VARCHAR(50) DEFAULT 'START',
    origem VARCHAR(50),
    context JSONB DEFAULT '{}',
    last_interaction TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_leads_professional_phone
        UNIQUE (professional_id, telefone_whatsapp)
);

CREATE TABLE tb_agendamentos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    lead_id UUID NOT NULL REFERENCES tb_leads(id),
    service_id UUID NOT NULL REFERENCES tb_servicos(id),
    professional_id UUID NOT NULL REFERENCES tb_professionals(id),
    data_hora_inicio TIMESTAMP NOT NULL,
    data_hora_fim TIMESTAMP NOT NULL,
    valor_pago DECIMAL(10, 2),
    data_confirmacao TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'EM_TRIAGEM'
        CHECK (status IN ('EM_TRIAGEM', 'PENDENTE', 'CONFIRMADO', 'CANCELADO', 'REMARCAR_PENDENTE')),
    midia_urls TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_blocked_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    professional_id UUID NOT NULL REFERENCES tb_professionals(id),
    data_hora_inicio TIMESTAMP NOT NULL,
    data_hora_fim TIMESTAMP NOT NULL,
    descricao VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_duvidas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    lead_id UUID NOT NULL REFERENCES tb_leads(id),
    professional_id UUID NOT NULL REFERENCES tb_professionals(id),
    mensagem_corpo TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_whatsapp_webhook_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    professional_id UUID REFERENCES tb_professionals(id) ON DELETE SET NULL,
    phone_number_id VARCHAR(100) NOT NULL,
    whatsapp_message_id VARCHAR(255) NOT NULL UNIQUE,
    event_type VARCHAR(80) NOT NULL,
    payload JSONB NOT NULL,
    processed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_agendamentos_periodo
    ON tb_agendamentos (professional_id, data_hora_inicio, data_hora_fim);

CREATE INDEX idx_bloqueios_periodo
    ON tb_blocked_events (professional_id, data_hora_inicio, data_hora_fim);

CREATE INDEX idx_leads_professional_whatsapp
    ON tb_leads (professional_id, telefone_whatsapp);

CREATE INDEX idx_whatsapp_integrations_phone_number
    ON tb_whatsapp_integrations (phone_number_id);

CREATE INDEX idx_whatsapp_webhook_events_phone_number
    ON tb_whatsapp_webhook_events (phone_number_id);
