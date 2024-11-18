### Grimoire Loyalty System

- Um pequeno pet-project para estudar `Spring Boot 3`, `PostgresSQL` e `row-locking`
- Um sistema de fidelidade para a bookstore

### Objetivos

1. Abraçar as características do SpringBoot valorizando o `RestControllerAdvice` e criando `light weight exceptions`
2. Permitir resgate concorrentemente

### Funcionalidades:

- [x] Cadastrar produtos
- [x] Cadastrar clientes e adicionar pontos
- [X] Fazer resgate de produtos

### Como Testar?

- Crie um produto `/api/products`
- Crie alguns clientes `/api/purchase/add`
- Tente resgatar concorretemente um item `/api/reedem`

Temos uma pasta chamadas `scripts` que pode ser utilizada para executar esse fluxo utilizando Java.

### E2E

Temos um arquivo `ExecuteE2ETest.java` que executa todos os fluxos e faz com que 100 usuários concorrentemente tente resgatar produtos físicos e produtos virtuais (sem estoque), de forma que o resultado final é:

- 100 produtos virtuais corretamente resgatados
- 50 produtos fisicos corretamente resgatados
- 50 produtos físicos com falta de estoque