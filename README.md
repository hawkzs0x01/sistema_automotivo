# 🚗 Sistema Automotivo - Gestão de Veículos

> Projeto acadêmico de um sistema web full-stack para gerenciamento de estoque de veículos, desenvolvido com Java/Spring Boot no back-end e JavaScript puro no front-end.

**Status do Projeto: Concluído ✔️**

---

### Tabela de Conteúdos
1. [Sobre o Projeto](#sobre-o-projeto)
2. [Funcionalidades](#funcionalidades)
3. [Tecnologias Utilizadas](#tecnologias-utilizadas)
4. [Como Executar o Projeto (Tutorial)](#como-executar-o-projeto)
5. [Como Usar e Testar a Aplicação](#como-usar-e-testar-a-aplicação)
6. [Estrutura da API REST](#estrutura-da-api-rest)

---

### Sobre o Projeto

O **Sistema Automotivo** é uma aplicação web desenvolvida para solucionar os desafios de gestão de estoque em concessionárias de veículos. O projeto substitui processos manuais e desorganizados por uma plataforma digital centralizada, permitindo o cadastro eficiente, a consulta com filtros dinâmicos e a manutenção do acervo de veículos.

A aplicação implementa um sistema de autenticação seguro com diferentes níveis de permissão, onde usuários comuns podem navegar pelo catálogo e expressar interesse de compra, enquanto administradores têm controle total sobre o inventário (CRUD de veículos, marcas e modelos).

---

### Funcionalidades

-   [x] **Autenticação Segura:** Sistema de registro e login com senhas criptografadas (BCrypt).
-   [x] **Autorização Baseada em Papéis:**
    -   **Usuário Comum (`ROLE_USER`):** Visualiza o catálogo, utiliza filtros e pode registrar interesse de compra em um veículo.
    -   **Administrador (`ROLE_ADMIN`):** Possui acesso total ao sistema, incluindo todas as funcionalidades de gerenciamento.
-   [x] **CRUD Completo de Veículos:** Administradores podem adicionar, visualizar, editar e remover veículos do estoque.
-   [x] **CRUD Completo de Catálogo:** Interface para administradores gerenciarem Marcas e Modelos.
-   [x] **Filtros Dinâmicos:** O catálogo de veículos pode ser filtrado em tempo real por Marca, Modelo, Ano e Preço Máximo.
-   [x] **Lógica de Venda ("Soft Delete"):** Usuários podem "comprar" (registrar interesse), o que altera o status do veículo para "Vendido" sem remover o registro do banco de dados, simulando um fluxo de negócio realista.
-   [x] **Validação de Dados:** O back-end valida os dados recebidos para garantir a integridade do banco (ex: preços não podem ser negativos).
-   [x] **Interface Reativa:** Front-end desenvolvido com JavaScript puro que consome a API REST, atualizando a tela dinamicamente sem a necessidade de recarregar a página.

---

### Tecnologias Utilizadas

| Camada         | Tecnologia                                                                                             |
|----------------|--------------------------------------------------------------------------------------------------------|
| **Back-end** | **Java 17**, **Spring Boot 3**, **Spring Security**, **Spring Data JPA**, **Hibernate**, **Maven** |
| **Front-end** | **HTML5**, **CSS3**, **JavaScript (Vanilla)** |
| **Banco de Dados** | **MySQL** |

---

### Como Executar o Projeto

Siga os passos abaixo para configurar e executar o projeto em seu ambiente local.

#### **1. Pré-requisitos**

Antes de começar, garanta que você tenha os seguintes softwares instalados:
-   [Java JDK](https://www.oracle.com/java/technologies/downloads/) (versão 17 ou superior)
-   [Apache Maven](https://maven.apache.org/download.cgi)
-   [MySQL Server](https://dev.mysql.com/downloads/mysql/)

#### **2. Configuração do Banco de Dados**

Execute os seguintes comandos no seu cliente MySQL para criar o banco de dados e o usuário da aplicação:
```sql
CREATE DATABASE sistema_automotivo;
CREATE USER 'seu_usuario'@'localhost' IDENTIFIED BY 'sua_senha';
GRANT ALL PRIVILEGES ON sistema_automotivo.* TO 'seu_usuario'@'localhost';
FLUSH PRIVILEGES;
```
*Lembre-se de substituir `seu_usuario` e `sua_senha` por suas credenciais.*

#### **3. Configuração da Aplicação**

Navegue até o arquivo `src/main/resources/application.properties` e altere as linhas abaixo com as credenciais que você acabou de criar:
```properties
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

#### **4. Execução**

Com tudo configurado, abra um terminal na pasta raiz do projeto e execute os comandos:

1.  **Limpar e compilar o projeto:**
    ```bash
    mvn clean install
    ```
    Aguarde a mensagem `BUILD SUCCESS`.

2.  **Iniciar o servidor:**
    ```bash
    mvn spring-boot:run
    ```
    A aplicação estará rodando na porta `8080`.

3.  **Acesse o sistema** abrindo seu navegador e indo para:
    👉 **`http://localhost:8080`**

---

### Como Usar e Testar a Aplicação

A aplicação é inicializada com dois usuários de teste para facilitar a demonstração.

#### **Credenciais de Teste**

| Perfil            | Usuário | Senha  |
|-------------------|---------|--------|
| 👤 **Administrador** | `admin` | `1234` |
| 👤 **Usuário Comum** | `user`  | `1234` |

#### **Fluxo de Teste Recomendado:**

1.  **Teste como Usuário Comum:**
    * Acesse `http://localhost:8080`.
    * Faça login com `user` / `1234`.
    * Navegue pelo catálogo. Use os filtros de "Marca", "Modelo", etc., e clique em "Filtrar".
    * Encontre um carro "Disponível" e clique em "Tenho Interesse". Confirme a ação e veja o status do carro mudar para "Vendido".
    * Note que os botões de gerenciamento ("Adicionar Veículo", "Editar", "Excluir") não estão visíveis.
    * Faça logout.

2.  **Teste como Administrador:**
    * Faça login com `admin` / `1234`.
    * Verifique que os botões "Gerenciar Catálogo" e "Adicionar Veículo" agora estão visíveis.
    * **CRUD de Catálogo:** Clique em "Gerenciar Catálogo", adicione uma nova marca, edite seu nome e depois a exclua.
    * **CRUD de Veículos:**
        * Clique em "Adicionar Veículo", preencha o formulário e salve. Verifique se o novo carro aparece na lista.
        * Clique em "Editar" no carro que você acabou de criar, altere o preço e salve. Verifique a alteração.
        * Clique em "Excluir" no mesmo carro e confirme. Verifique se ele foi removido da lista.

---

### Estrutura da API REST

A API segue os padrões RESTful. Aqui estão os principais endpoints:

| Método   | URL                       | Descrição                                 | Acesso      |
|----------|---------------------------|-------------------------------------------|-------------|
| `POST`   | `/api/auth/register`      | Registra um novo usuário.                 | Público     |
| `POST`   | `/api/auth/login`         | Autentica um usuário e cria uma sessão.   | Público     |
| `GET`    | `/api/veiculos`           | Lista todos os veículos (aceita filtros). | Público     |
| `POST`   | `/api/veiculos`           | Adiciona um novo veículo.                 | Admin       |
| `PUT`    | `/api/veiculos/{id}`      | Atualiza um veículo existente.            | Admin       |
| `DELETE` | `/api/veiculos/{id}`      | Exclui um veículo permanentemente.        | Admin       |
| `POST`   | `/api/veiculos/{id}/vender` | Marca um veículo como vendido.            | Autenticado |
| `GET`    | `/api/marcas`             | Lista todas as marcas.                    | Público     |
| `POST`   | `/api/marcas`             | Adiciona uma nova marca.                  | Admin       |
| `GET`    | `/api/modelos`            | Lista todos os modelos.                   | Público     |
| `POST`   | `/api/modelos`            | Adiciona um novo modelo.                  | Admin       |

---