# WellWork – Estação Inteligente de Conforto no Trabalho

A **WellWork** é uma solução completa composta por um **dispositivo físico (Arduino)** e uma **API em Java Spring Boot**, projetada para monitorar e melhorar o conforto ambiental no ambiente de trabalho.  
A API recebe medições de sensores, calcula um índice de conforto, gera alertas, registra dados históricos e fornece endpoints seguros para consulta.

Este projeto foi desenvolvido com foco em:

- **Conforto térmico, luminoso, acústico e da qualidade do ar**
- **Monitoramento contínuo**
- **Alertas automáticos**
- **Persistência de dados**
- **Autenticação JWT**
- **Estrutura modular, clara e escalável**
- **Banco de dados H2**
- **Simulação das medições via arquivo JSON**

---

## 📡 Sensores simulados

O sistema aceita leituras correspondentes aos sensores usados no Arduino:

- **Temperatura** (DHT22)  
- **Umidade** (DHT22)  
- **Luminosidade** (BH1750)  
- **Ruído** (KY-037)  
- **Qualidade do ar** (valor numérico genérico)

As leituras são enviadas em JSON para a API.

---

## 🧠 Índice de Conforto

A API calcula um **Comfort Index** (0 a 100) com base nos sensores.

Faixas:
- `>= 75` → ambiente confortável  
- `< 75` → desconforto → gera alerta automaticamente

Cada parâmetro (temperatura, umidade, luminosidade, ruído, ar) recebe um peso e indicação do quão longe está da faixa ideal.

O sistema identifica o **pior fator** e usa isso para determinar o tipo de alerta.

---

## 🔔 Alertas Automáticos

Quando o índice de conforto cai abaixo de 75:

1. A API identifica o sensor crítico  
2. Gera um alerta no banco de dados  
3. Retorna um `AlertDTO` com:
   - mensagem descritiva
   - tipo de alerta  
   - severidade normalizada  
   - recomendação automática  

---

## 🔒 Autenticação e Login (JWT)

A API usa **Spring Security + JWT**.

Fluxo:

1. `/auth/register`  
   Criação de usuário (salvo no H2).

2. `/auth/login`  
   Retorna um token JWT válido.

3. Todas as rotas protegidas exigem:  
   `Authorization: Bearer <token>`

A chave JWT e tempo de expiração vêm de:
jwt.secret=MEUSEGREDOAQUI123456789012345678
jwt.expiration-ms=86400000

---

## 🛢 Banco de Dados H2

Banco de dados em memória para desenvolvimento.

Acesso ao console:
/h2-console

Configuração:
spring.datasource.url=jdbc:h2:mem:wellworkdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update

---

## 🗂 Estrutura do Projeto
br/com/fiap/wellwork
├── controller
│ ├── AuthController
│ ├── MeasurementController
│ └── ComfortController
├── service
│ ├── AuthService
│ ├── MeasurementService
│ ├── ComfortService
│ └── impl/...
├── model
│ ├── entity/
│ ├── enums/
│ └── vo/
├── repository
├── security
│ ├── JwtUtil
│ ├── JwtFilter
│ └── SecurityConfig
├── dto/
└── WellworkApplication.java


---

## 📥 Exemplo de Medição Recebida pela API
POST /measurements

{
"temperature": 24.5,
"humidity": 55,
"luminosity": 350,
"noise": 45,
"airQuality": 30,
"location": "Escritório 1",
"timestamp": "2025-11-13T10:22:00"
}

Resposta quando confortável:
204 No Content


Resposta quando desconfortável:
{
"message": "Temperatura 18°C está fora da faixa ideal.",
"type": "TEMPERATURE",
"severity": 0.32,
"recommendation": "Ajuste o ar-condicionado ou abra a janela."
}


---

## 📊 Exemplo de JSON para simulação via arquivo

Você pode criar um arquivo `leituras.json`:
[
{
"temperature": 22,
"humidity": 50,
"luminosity": 500,
"noise": 38,
"airQuality": 20,
"location": "Sala 1"
},
{
"temperature": 18,
"humidity": 30,
"luminosity": 200,
"noise": 55,
"airQuality": 70,
"location": "Sala 2"
}
]

E enviar cada linha ao endpoint `/measurements`.

---

## 🔧 Configuração via application.properties
server.port=8080

spring.datasource.url=jdbc:h2:mem:wellworkdb
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

jwt.secret=MEUSEGREDOAQUI123456789012345678
jwt.expiration-ms=86400000


---

## 🧪 Testando com cURL

### 1. Registrar
curl -X POST http://localhost:8080/auth/register

-H "Content-Type: application/json"
-d "{"username":"admin","password":"123456"}"


### 2. Login
curl -X POST http://localhost:8080/auth/login

-H "Content-Type: application/json"
-d "{"username":"admin","password":"123456"}"

### 3. Enviar medição
curl -X POST http://localhost:8080/measurements

-H "Authorization: Bearer <TOKEN>"
-H "Content-Type: application/json"
-d "{"temperature":30,"humidity":30,"luminosity":200,"noise":55,"airQuality":70}"


---

## 🏛 Tecnologias Utilizadas

- Java 17  
- Spring Boot 3  
- Spring Security  
- JWT (jjwt-api, jjwt-impl, jjwt-jackson)  
- H2 Database  
- Lombok  
- Maven  

---

## 📌 Status Atual do Projeto

✔ Estrutura completa  
✔ CRUD de medições  
✔ Cálculo do índice de conforto  
✔ Geração automática de alertas  
✔ DTOs organizados  
✔ VO (Value Objects) para cálculos ambientais  
✔ JWT funcional com filtros  
✔ H2 funcionando  
✔ Controllers e Services completos  
✔ Documentação finalizada  

---

## 📎 Próximos Passos (Sugestões)

- Adicionar Swagger/OpenAPI  
- Criar front-end React/Next.js  
- Implementar WebSockets para alertas em tempo real  
- Gerar relatórios PDF  
- Suporte a múltiplos dispositivos/salas  

---

## 🧑‍💻 Autoria

Projeto desenvolvido como formação educacional, com apoio do ChatGPT (“Astolfo”), utilizando boas práticas de engenharia de software, arquitetura limpa e padrões modernos.


---

## 🏛 Tecnologias Utilizadas

- Java 21  
- Spring Boot 3  
- Spring Security  
- JWT (jjwt-api, jjwt-impl, jjwt-jackson)  
- H2 Database  
- Lombok  
- Maven  

---

## 📌 Status Atual do Projeto

✔ Estrutura completa  
✔ CRUD de medições  
✔ Cálculo do índice de conforto  
✔ Geração automática de alertas  
✔ DTOs organizados  
✔ VO (Value Objects) para cálculos ambientais  
✔ JWT funcional com filtros  
✔ H2 funcionando  
✔ Controllers e Services completos  
✔ Documentação finalizada  

---

## 📎 Próximos Passos (Sugestões)

- Adicionar Swagger/OpenAPI  
- Criar front-end React/Next.js  
- Implementar WebSockets para alertas em tempo real  
- Gerar relatórios PDF  
- Suporte a múltiplos dispositivos/salas  

---


