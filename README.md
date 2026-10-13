# 🚀 Explorando Padrões de Projetos com Spring Boot

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Spring_Cloud-OpenFeign-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Cloud OpenFeign" />
  <img src="https://img.shields.io/badge/Database-H2_In--Memory-003B57?style=for-the-badge&logo=h2&logoColor=white" alt="H2 Database" />
  <img src="https://img.shields.io/badge/Documentação-Swagger_OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger OpenAPI" />
  <img src="https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven" />
</p>

---

## 📌 Sobre o Projeto

Este projeto representa um marco importante na minha jornada de desenvolvimento: é a **minha primeira API REST** desenvolvida em **Java** com **Spring Boot**! 

Ele foi construído como solução para o **Desafio de Projeto** do bootcamp promovido pela **DIO (Digital Innovation One)** em parceria com o **Itaú**.

O foco central foi sair da teoria e vivenciar na prática como os **Padrões de Projeto (Design Patterns do GoF)** são aplicados no ecossistema Spring:
* **Singleton**
* **Strategy**
* **Facade**

A API gerencia o cadastro de clientes, integrando-se automaticamente à API pública do **[ViaCEP](https://viacep.com.br/)** para buscar e autopreencher o endereço a partir do CEP informado, armazenando tudo no banco de dados em memória **H2**.

---

## 🎯 Padrões de Projeto na Prática

### 1. 🔷 Singleton
* **O que propõe:** Garantir uma instância única de uma classe por aplicação, economizando memória e centralizando o estado.
* **No Spring Boot:** O Spring gerencia todos os seus Beans por padrão como Singletons no seu IoC Container (`Inversion of Control`).
* **No Código:** As anotações `@Service` (`ClientServiceImpl`), `@RestController` (`ClientRestController`), `@Repository` (`ClientRepository`, `AddressRepository`) e `@FeignClient` (`ViaCepService`) são instanciadas apenas uma vez e injetadas via `@Autowired`, evitando criações desnecessárias de objetos.

### 2. 🔷 Strategy
* **O que propõe:** Definir uma interface comum para uma família de operações, tornando o comportamento intercambiável sem impactar quem o utiliza.
* **No Spring Boot:** Desacoplamento entre a definição da regra de negócio e sua execução real.
* **No Código:** A interface `ClientService` define os métodos esperados (estratégia). A classe `ClientServiceImpl` implementa essa estratégia. O `ClientRestController` depende somente da interface `ClientService`, permitindo alternar ou criar novas estratégias de negócio sem alterar o controlador.

### 3. 🔷 Facade
* **O que propõe:** Fornecer uma interface simples e unificada para gerenciar múltiplos subsistemas ou integrações complexas.
* **No Spring Boot:** Esconder a complexidade de múltiplos serviços, repositórios e chamadas de rede.
* **No Código:** 
  * `ClientServiceImpl` atua como Facade: quem cadastra um cliente chama apenas `save(client)`, enquanto internamente o serviço verifica o banco local H2, consome a API externa do ViaCEP via OpenFeign se necessário, salva o endereço e vincula ao cliente.
  * `ClientRestController` atua como a Facade externa da aplicação, fornecendo rotas REST limpas para clientes HTTP.

---

## 🏛️ Arquitetura e Fluxo da Aplicação

### Visão em Camadas

```
[ Cliente HTTP / Swagger UI ]
              │
              ▼
   1. ClientRestController  ──► (Facade REST: expõe os endpoints HTTP)
              │
              ▼
   2. ClientService         ──► (Strategy: interface com as operações de negócio)
              │
              ▼
   3. ClientServiceImpl     ──► (Facade & Singleton: orquestra os subsistemas)
         │           │
         │           └───► 4. ViaCepService (OpenFeign: consulta viacep.com.br)
         │
         └───────────────► 5. Repositories JPA (H2: persiste Clientes e Endereços)
```

### Como funciona o cadastro (`POST /clients`):

1. **Requisição:** O cliente envia nome e apenas o CEP desejado.
2. **Verificação Local:** O sistema checa se o endereço já existe no banco H2.
3. **Consulta Externa (se necessário):** Se o endereço for novo, o Feign Client faz uma requisição HTTP para a API do ViaCEP e traz os dados completos (rua, bairro, cidade, UF, etc.).
4. **Persistência:** O endereço é salvo no banco e vinculado ao novo cliente.
5. **Retorno:** A API devolve o cliente cadastrado com o endereço completo preenchido.

---

## 🛠️ Tecnologias Utilizadas

* **Java 21**
* **Spring Boot**
* **Spring Web MVC** (Construção dos controladores REST)
* **Spring Data JPA** (Persistência e mapeamento objeto-relacional)
* **Spring Cloud OpenFeign** (Cliente HTTP declarativo para consumir o ViaCEP)
* **H2 Database** (Banco de dados em memória)
* **SpringDoc OpenAPI / Swagger UI** (Documentação interativa da API)
* **Maven** (Gerenciador de dependências e build)

---

## 🌐 Endpoints da API

| Método | Rota | Descrição | Status |
| :--- | :--- | :--- | :--- |
| `GET` | `/clients` | Lista todos os clientes cadastrados | `200 OK` |
| `GET` | `/clients/{id}` | Busca um cliente específico por ID | `200 OK` |
| `POST` | `/clients` | Cadastra cliente e busca endereço via CEP | `200 OK` |
| `PUT` | `/clients/{id}` | Atualiza os dados de um cliente existente | `200 OK` |
| `DELETE` | `/clients/{id}` | Remove um cliente pelo ID | `200 OK` |

### Exemplo de Envio (`POST /clients`)

```json
{
  "name": "Pedro Ramon",
  "address": {
    "cep": "01001000"
  }
}
```

### Exemplo de Resposta (Endereço enriquecido pelo ViaCEP)

```json
{
  "id": 1,
  "name": "Pedro Ramon",
  "address": {
    "cep": "01001-000",
    "logadouro": "Praça da Sé",
    "complemento": "lado ímpar",
    "bairro": "Sé",
    "localidade": "São Paulo",
    "uf": "SP",
    "ibge": "3550308",
    "gia": "1004",
    "ddd": "11",
    "siafi": "7107"
  }
}
```

---

## 📖 Swagger & Console H2

Com a aplicação rodando localmente:

* **Swagger UI (Documentação Interativa):**  
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **Console do Banco H2:**  
  [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
  *JDBC URL:* `jdbc:h2:mem:testdb` | *User:* `sa` | *Password:* *(em branco)*

---

## 🚀 Como Executar Localmente

### Pré-requisitos
* **JDK 21** instalado
* **Git** instalado

### Passo a Passo

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/Pedro-Ramon2608/dio-itau-design-patterns-spring.git
   ```

2. **Entrar no diretório:**
   ```bash
   cd dio-itau-design-patterns-spring
   ```

3. **Rodar a aplicação:**
   * **Windows:**
     ```cmd
     .\mvnw.cmd spring-boot:run
     ```
   * **Linux/macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```

4. **Testar os endpoints:**
   Abra o navegador em [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

---

## 📁 Estrutura de Pastas

Estrutura organizada por responsabilidades no pacote principal:

```plaintext
src/main/java/com/dio/dio_itau_design_patterns_spring/
├── controller/
│   └── ClientRestController.java      # Camada Web: Endpoints REST (Facade)
├── model/
│   ├── Client.java                     # Entidade de Cliente
│   ├── Address.java                    # Entidade de Endereço (dados ViaCEP)
│   ├── ClientRepository.java           # Persistência de Clientes (Spring Data JPA)
│   └── AddressRepository.java          # Persistência de Endereços (Spring Data JPA)
├── service/
│   ├── ClientService.java              # Interface de regras de negócio (Strategy)
│   ├── ViaCepService.java              # Cliente HTTP para ViaCEP (OpenFeign)
│   └── impl/
│       └── ClientServiceImpl.java      # Implementação e orquestração (Facade & Strategy)
└── DioItauDesignPatternsSpringApplication.java # Classe inicializadora Spring Boot
```

---

## 👨‍💻 Autor

Desenvolvido por **Pedro Ramon**:

* **GitHub:** [@Pedro-Ramon2608](https://github.com/Pedro-Ramon2608)
* **Projeto:** Desafio do Bootcamp DIO + Itaú

---

## 🤝 Agradecimentos

Agradecimentos à **[Digital Innovation One (DIO)](https://www.dio.me/)** e ao **[Itaú Unibanco](https://www.itau.com.br/)** pelo bootcamp e pela oportunidade de aprendizado prático.

---
<p align="center">Feito com ☕ e dedicação como a minha primeira API REST em Java & Spring Boot!</p>
