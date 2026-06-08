# 🏗️ Obra Certa - App Mobile

O **Obra Certa** é um aplicativo Android desenvolvido para auxiliar profissionais da construção civil no gerenciamento de seus projetos, cálculo de materiais e acompanhamento de tarefas diárias.

Este repositório contém o código-fonte do aplicativo mobile (Frontend e Backend local), construído de forma nativa utilizando a linguagem recomendada pelo Google para o ecossistema mobile.

## 📱 Telas do Sistema

<p align="center">
  <img src="imagens/home.png" width="250" />
  <img src="imagens/calc.png" width="250" />
  <img src="imagens/orc.png" width="250" />
</p>

## 🚀 Funcionalidades

* **Autenticação Local:** Sistema de Login e Cadastro de usuários totalmente funcional com validação de credenciais e prevenção de e-mails duplicados.
  
* **Persistência de Dados (SQLite):** Implementação de banco de dados local através da biblioteca **Room**. O sistema possui o **CRUD completo** (Create, Read, Update, Delete) para as entidades:
  * Obras/Projetos
  * Tarefas
  * Materiais
  * Usuários
* **Calculadora e Orçamentação Dinâmica:** * Cálculo de área (m²) com base em altura e largura com margem de segurança de 10% (perda de material).
  * Vínculo dinâmico entre o material calculado e as obras salvas no banco de dados.
  * Atualização automática de valores (Custo Total Acumulado) utilizando o ciclo de vida do Android (`onResume`).
* **Integração com WhatsApp:** Geração automática de lista de materiais formatada em texto e redirecionamento via *Intent Implícita* (Link `wa.me`) direto para o aplicativo do WhatsApp para solicitação de cotações com lojistas.
* **Gerenciador de Tarefas:** Criação de checklist por projeto com lógica interativa de CheckBoxes (efeito `strikethrough` para tarefas concluídas) salvo no banco de dados.
* **Design System e UI/UX:** * Interface totalmente padronizada utilizando margens de respiro (`30dp`) e áreas de toque acessíveis (`60dp` para botões).
  * Suporte nativo ao formato **Edge-to-Edge** (integração da UI com a Status Bar do sistema).
  * Formulários encapsulados em `ScrollViews` com `fillViewport` para prevenir conflitos visuais com o teclado do dispositivo.

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Kotlin
* **IDE:** Android Studio
* **Interface (UI):** XML (Views tradicionais)
* **Banco de Dados:** SQLite (via biblioteca Room Database)
* **Gerenciamento de Dependências:** Gradle
* **Controle de Versão:** Git & GitHub

## 📂 Estrutura de Telas

O fluxo do aplicativo está dividido nas seguintes Activities principais:
1. `LoginActivity` / `CadastroActivity` / `RecuperarActivity`: Módulo de entrada e autenticação.
2. `HomeActivity`: Dashboard principal (Resumo).
3. `ProjetosActivity`: Lista de clientes e criação de novas obras.
4. `DetalhesProjetoActivity`: Hub da obra contendo Abas dinâmicas para Orçamento (Materiais) e Tarefas.
5. `CalculadoraActivity`: Ferramenta matemática com injeção automática do `ProjetoId`.
6. `PerfilActivity`: Informações e encerramento de sessão do usuário logado.
