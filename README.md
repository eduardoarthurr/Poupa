# 🥭 POUPA - CRM Conversacional Inteligente

O **POUPA** é um MVP de CRM Conversacional desenvolvido para profissionais liberais (Barbeiros e Trancistas). O sistema automatiza a triagem, FAQ e coleta de dados de agendamento via WhatsApp, priorizando um fluxo híbrido: o bot prepara o terreno e o profissional detém o controle final da agenda.

## 🚀 Tecnologias e Infraestrutura

- **Backend:** Java 21, Spring Boot 3.x, Spring Data JPA.
- **Frontend:** React.js + Tailwind CSS (Netlify).
- **Banco de Dados:** PostgreSQL 16 (Render.com).
- **Mensageria:** WhatsApp Business Cloud API (Meta Oficial).
- **Arquitetura:** Máquina de Estados (State Machine) Persistida.

## 🛠️ Funcionalidades Principais

### 1. Fluxo Híbrido & Kill-Switch
- **Detecção de Intervenção:** O sistema detecta via Webhook quando o profissional envia uma mensagem manual e desativa o bot automaticamente (`is_bot_active = FALSE`).
- **Reativação:** O bot retoma o atendimento apenas após a mensagem gatilho: *"Atendimento finalizado!"*.

### 2. Modos de Atendimento Dinâmicos
- **Modo A (Barbearias):** Fluxo ágil com qualificação por foto opcional.
- **Modo B (Trancistas):** Fluxo técnico com qualificação obrigatória (envio de 2 fotos para avaliação de comprimento e referência).

### 3. Motor de Disponibilidade (Anti-Overbooking)
- Cálculo de slots baseado na duração de cada serviço.
- Cruzamento de dados entre Grade de Trabalho, Agendamentos Confirmados e **Eventos de Bloqueio** (almoço, folgas, compromissos manuais).

### 4. Gestão Visual por Etiquetas (Meta Business Suite)
- 🔵 **Azul:** Novo Lead / Triagem.
- 🟡 **Amarela:** Pedido de Remarcação.
- 🟠 **Laranja:** Dúvida (Handoff humano).
- 🔴 **Vermelha:** Cancelamento realizado.
- 🟢 **Verde:** Agendamento Confirmado.

## 📈 Dashboard de Gestão
- Visão de agenda semanal dinâmica.
- Métricas de faturamento (Diário, Semanal, Mensal).
- Taxa de conversão real (Leads vs. Agendamentos).
- Links diretos para WhatsApp (`wa.me`) com mensagens pré-preenchidas.

## ⚙️ Como rodar o projeto localmente

### Pré-requisitos
- JDK 21
- Maven 3.x
- PostgreSQL 16

### Configuração
1. Clone o repositório:
   ```bash
   git clone https://github.com/SEU_USUARIO/poupa-api.git
2. Crie o arquivo src/main/resources/application.local.properties e adicione suas credenciais do banco de dados:
   ```bash
   spring.datasource.url=SEU_LOCAL_HOST
   spring.datasource.username=SEU_USUARIO
   spring.datasource.password=SUA_SENHA

Copyright (C) 2026 Eduardo Arthur Rodovalho Alves Filho

Todos os direitos reservados. 

Este código-fonte é disponibilizado apenas para fins de visualização e avaliação de portfólio. 
Não é permitida a cópia, modificação, distribuição ou uso comercial deste software, 
total ou parcial, sem a autorização prévia e por escrito do detentor dos direitos autorais.

O uso do código para fins de aprendizado pessoal é permitido, mas a reprodução do sistema 
para fins de hospedagem ou venda é estritamente proibida.
