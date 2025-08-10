# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

AlgExpress is a complete order and delivery management system for pizzerias, built with Spring Boot 3.5.4 and Java 21. It's a pizza delivery management application that handles everything from customer registration to delivery tracking.

## Technology Stack

- **Java 21** - Core language
- **Spring Boot 3.5.4** - Web framework
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Database (production)
- **Lombok** - Code generation for boilerplate reduction
- **Maven** - Build tool and dependency management

## Architecture

The project follows Domain-Driven Design (DDD) principles with these bounded contexts:
- **Orders** (Pedidos) - Order management and items
- **Customers** (Clientes) - Customer registration and relationships  
- **Menu** (Cardápio) - Products, pizzas and ingredients
- **Deliveries** (Entregas) - Logistics and fee calculation
- **Financial** (Financeiro) - Payments and fees

### Layer Structure
```
┌─ Presentation Layer (Controllers/API)
├─ Application Layer (Services/Use Cases)  
├─ Domain Layer (Entities/Value Objects)
└─ Infrastructure Layer (Repositories/External Services)
```

### API Structure
The RESTful API is organized by bounded context:
- `/api/v1/clientes` - Customer management
- `/api/v1/enderecos` - Delivery addresses
- `/api/v1/cardapio` - Pizzas and ingredients
- `/api/v1/pedidos` - Order creation and tracking
- `/api/v1/entregas` - Delivery control
- `/api/v1/relatorios` - Analytics data

## Common Development Commands

### Build and Run
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Run the application
mvn spring-boot:run

# Package the application
mvn clean package

# Build without tests
mvn clean package -DskipTests
```

### Development Workflow
```bash
# Run in development mode (auto-reload)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Run tests with coverage
mvn test jacoco:report

# Integration tests
mvn verify
```

## Project Structure

```
src/main/java/br/com/alg/algexpress/
├── AlgexpressApplication.java          # Main Spring Boot application
└── [Domain packages to be implemented]

src/main/resources/
├── application.properties              # Main configuration
├── static/                            # Static web resources
└── templates/                         # Web templates

src/test/java/
└── AlgexpressApplicationTests.java     # Main test class
```

## Configuration

The application uses `application.properties` for configuration. Key areas to configure:
- Database connection (PostgreSQL)
- Server port (default: 8080) 
- API versioning
- Delivery settings (rates, distance limits)

## Database

- **Production**: PostgreSQL
- **Development**: Can be configured for H2 in-memory database
- Uses JPA/Hibernate for ORM
- Lombok for entity generation

## Development Notes

- This is a fresh Spring Boot project with minimal implementation
- The main bounded contexts and entities need to be implemented
- The project is set up with Maven wrapper (`mvnw`) for consistent builds
- Uses Java 21 features and Spring Boot 3.x patterns
- Follows pragmatic DDD approach without over-engineering

## Testing

- Uses Spring Boot Test framework
- Test classes should mirror the main package structure
- Integration tests should use `@SpringBootTest`
- Unit tests should mock dependencies appropriately