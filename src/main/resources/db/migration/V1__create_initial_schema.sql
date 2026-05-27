CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE tb_professionals (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(20) NOT NULL CHECK (categoria IN ('BARBEIRO', 'TRANCISTA')),
    whatsapp_oficial VARCHAR(20) UNIQUE NOT NULL,
    expediente_inicio TIME NOT NULL,
    expediente_fim TIME NOT NULL,
    google_calendar_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    professional_id UUID NOT NULL REFERENCES tb_professionals(id),
    telefone_whatsapp VARCHAR(20) UNIQUE NOT NULL,
    is_bot_active BOOLEAN DEFAULT TRUE,
    current_state VARCHAR(50) DEFAULT 'START',
    origem VARCHAR(50),
    context JSONB DEFAULT '{}',
    last_interaction TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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

CREATE INDEX idx_agendamentos_periodo
    ON tb_agendamentos (professional_id, data_hora_inicio, data_hora_fim);

CREATE INDEX idx_bloqueios_periodo
    ON tb_blocked_events (professional_id, data_hora_inicio, data_hora_fim);

CREATE INDEX idx_lead_whatsapp
    ON tb_leads (telefone_whatsapp);
