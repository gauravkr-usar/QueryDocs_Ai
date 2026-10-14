# 📄 QueryDocs AI - Intelligent Document Analysis & Conversational RAG Platform

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen?logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Spring%20AI-1.0.0-blue?logo=spring&logoColor=white" alt="Spring AI" />
  <img src="https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/React-19-61DAFB?logo=react&logoColor=black" alt="React 19" />
  <img src="https://img.shields.io/badge/Vite-8-646CFF?logo=vite&logoColor=white" alt="Vite" />
  <img src="https://img.shields.io/badge/Tailwind%20CSS-v4-38B2AC?logo=tailwindcss&logoColor=white" alt="Tailwind CSS" />
  <img src="https://img.shields.io/badge/PostgreSQL-PgVector-336791?logo=postgresql&logoColor=white" alt="PgVector" />
  <img src="https://img.shields.io/badge/Docker-Enabled-2496ED?logo=docker&logoColor=white" alt="Docker" />
</p>

---

## 🌟 Overview

**QueryDocs AI** is an enterprise-ready, end-to-end Retrieval-Augmented Generation (RAG) platform that empowers users to upload documents (PDFs, Word documents, text files), parse and chunk their content, store high-dimensional embeddings in **PgVector**, and engage in intelligent, context-aware conversations powered by cutting-edge Large Language Models (Groq / OpenAI / Gemini).

The platform also provides deep document intelligence features, allowing users to extract clauses, key concepts, and facts, as well as inspect granular document chunks with similarity scores.

---

## 🚀 Key Features

- **📑 Multi-Document Upload & Processing**:
  - Drag-and-drop document upload with real-time progress indicators.
  - Automatic text extraction, semantic chunking, and metadata tagging.
- **🧠 Retrieval-Augmented Generation (RAG)**:
  - Vector search backed by PostgreSQL with `pgvector` extension (HNSW index, cosine distance).
  - Fast local or cloud embeddings (Spring AI Transformers / OpenAI `text-embedding-3-small`).
  - LLM integration supporting Groq (Llama 3.3 70B), OpenAI GPT-4o, and Google Gemini.
- **💬 Conversational Memory & Multi-Turn Chat**:
  - Persistent conversation history stored in relational database.
  - Context retention across turns with relevant document citation references.
- **🔍 Document Intelligence & Deep Search**:
  - Extract specific contract clauses, business concepts, and key facts.
  - Document chunk viewer to inspect retrieved text fragments and relevance scores.
- **🔒 Security & Multi-Tenant Isolation**:
  - JWT-based authentication (Register / Login / Token Refresh).
  - Strict user-level document isolation ensuring private data privacy.
- **🎨 Sleek Modern Frontend**:
  - Built with React 19, TypeScript, Tailwind CSS v4, and Framer Motion.
  - Interactive chat interface, markdown rendering with syntax highlighting, and responsive layout.

---

## 🏗️ Architecture

```
                       +-------------------------------+
                       |   React 19 Frontend (Vite)    |
                       |    Tailwind CSS + Zustand     |
                       +---------------+---------------+
                                       |
                           HTTP / REST | (Port 3000 -> 8081)
                                       v
                       +-------------------------------+
                       |   Spring Boot Backend (8081)  |
                       | Spring Security + JWT Auth    |
                       +-------+---------------+-------+
                               |               |
                 Document RAG  |               | LLM Inference
                               v               v
            +--------------------+    +----------------------+
            |  PostgreSQL 16     |    |  Groq / OpenAI /     |
            |  (pgvector HNSW)   |    |  Google Gemini API   |
            +--------------------+    +----------------------+
```

---

## 🛠️ Tech Stack

### **Backend**
- **Language**: Java 21 (OpenJDK LTS)
- **Framework**: Spring Boot 3.5.x
- **AI Engine**: Spring AI (OpenAI API client, Transformers embedding model)
- **Vector Database**: PostgreSQL 16 + `pgvector`
- **Security**: Spring Security + JWT (JSON Web Tokens)
- **Persistence**: Spring Data JPA / Hibernate
- **API Documentation**: SpringDoc OpenAPI / Swagger UI

### **Frontend**
- **Framework**: React 19 + TypeScript
- **Bundler**: Vite
- **Styling**: Tailwind CSS v4
- **State Management**: Zustand
- **HTTP Client**: Axios with interceptors
- **Animation**: Framer Motion
- **Icons & Markdown**: Lucide React, React Markdown, Remark GFM, PrismJS

### **Infrastructure & Tools**
- **Containerization**: Docker & Docker Compose (`pgvector/pgvector:pg16`)
- **Build Tools**: Maven (`mvnw`), npm

---

## 📁 Repository Structure

```
QueryDocs_Ai/
├── .env.example                     # Environment variables template
├── docker-compose.yml               # PgVector container definition
├── init.sql                         # PostgreSQL vector extension initializer
├── pom.xml                          # Maven build configuration
├── src/                             # Spring Boot backend source code
│   └── main/
│       ├── java/com/substring/querydocs/
│       │   ├── config/              # Security, CORS, AI & VectorStore config
│       │   ├── controller/          # Auth, Chat, Document REST endpoints
│       │   ├── dto/                 # Data Transfer Objects
│       │   ├── entity/              # User, Document, Chunk, Conversation entities
│       │   ├── repository/          # Spring Data JPA repositories
│       │   └── service/             # RAG, Document processing, LLM services
│       └── resources/
│           ├── application.yaml     # Application configuration
│           └── application-dev.yml  # Development profile configurations
├── frontend/
│   └── querydocs-frontend/          # React + Vite frontend source code
│       ├── src/
│       │   ├── components/          # UI components (Chat, UploadModal, etc.)
│       │   ├── pages/               # Application pages
│       │   └── store/               # Zustand state stores
│       ├── package.json
│       └── vite.config.ts
└── docs/                            # Architecture & design documentation
```

---

## ⚡ Getting Started

### Prerequisites
- **Java 21+**
- **Node.js 18+** & **npm**
- **Docker & Docker Compose** (Optional if using cloud PostgreSQL like Neon)

---

### Step 1: Clone the Repository
```bash
git clone https://github.com/gauravkr-usar/QueryDocs_Ai.git
cd QueryDocs_Ai
```

---

### Step 2: Configure Environment Variables
Copy `.env.example` to `.env`:
```bash
cp .env.example .env
```
Fill in your API keys in `.env`:
```env
# AI Models
OPENAI_API_KEY=your_openai_key
GROQ_API_KEY=your_groq_key
GEMINI_API_KEY=your_gemini_key

# JWT Secret
JWT_SECRET=your_256_bit_random_secret_key

# Database (Local Docker or Remote Neon)
DB_HOST=localhost
DB_PORT=5434
DB_NAME=querydocs
DB_USER=postgres
DB_PASSWORD=postgres
```

---

### Step 3: Start Vector Database (via Docker)
If using local Docker for PostgreSQL + `pgvector`:
```bash
docker compose up -d
```

---

### Step 4: Run the Backend
```bash
# On Windows
.\mvnw.cmd spring-boot:run

# On Linux / macOS
./mvnw spring-boot:run
```
The backend will launch at **`http://localhost:8081`**.
- **Swagger Documentation**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

---

### Step 5: Run the Frontend
```bash
cd frontend/querydocs-frontend
npm install
npm run dev
```
The frontend dev server will launch at **`http://localhost:3000`**.

---

## 🔌 API Endpoints Overview

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/auth/register` | Register new user |
| `POST` | `/api/v1/auth/login` | Login and receive JWT |
| `POST` | `/api/v1/documents/upload` | Upload & vectorize document |
| `GET` | `/api/v1/documents` | List uploaded user documents |
| `DELETE`| `/api/v1/documents/{id}` | Delete document and related vector embeddings |
| `GET` | `/api/v1/documents/{id}/chunks` | View extracted chunks for a document |
| `POST` | `/api/v1/chat/query` | Ask questions with conversational RAG |
| `GET` | `/api/v1/chat/history/{id}` | Fetch previous conversation messages |

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!
Feel free to open an issue or submit a pull request.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---


