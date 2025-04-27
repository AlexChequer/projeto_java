# projeto_java
Projeto de Java do 3o semestre de Ciência da Computação

Membros:
-  Alex Chequer
-  Gustavo Santana
-  Gustavo Valente

## Requisitos

- Java 17 ou superior
- Maven
- MySQL

## Como executar

### Executar o sistema

```bash
./run.bat  # No Windows
# ou
./mvn spring-boot:run
```

### Executar os testes 

```bash
./test.bat  # No Windows
# ou
./mvn spring-boot:run -Dspring-boot.run.profiles=test
```

## APIs disponíveis

### Categorias
- `GET /category` - Lista todas as categorias
- `GET /category/{id}` - Obtém uma categoria específica
- `POST /category` - Cria uma nova categoria
- `PUT /category/{id}` - Atualiza uma categoria
- `DELETE /category/{id}` - Remove uma categoria

### Produtos
- `GET /item` - Lista todos os produtos
- `GET /item/{id}` - Obtém um produto específico
- `GET /item/category/{categoryId}` - Lista produtos por categoria
- `POST /item` - Cria um novo produto
- `PUT /item/{id}` - Atualiza um produto
- `DELETE /item/{id}` - Remove um produto

### Clientes
- `GET /client` - Lista todos os clientes
- `GET /client/{id}` - Obtém um cliente específico
- `POST /client` - Cria um novo cliente
- `PUT /client/{id}` - Atualiza um cliente
- `DELETE /client/{id}` - Remove um cliente

### Administradores
- `GET /admin` - Lista todos os administradores
- `GET /admin/{id}` - Obtém um administrador específico
- `POST /admin` - Cria um novo administrador
- `PUT /admin/{id}` - Atualiza um administrador
- `DELETE /admin/{id}` - Remove um administrador

### Carrinho
- `GET /cart/{clientId}` - Obtém o carrinho de um cliente
- `POST /cart/{clientId}/items` - Adiciona um item ao carrinho
- `PUT /cart/{clientId}/items/{itemId}` - Atualiza a quantidade de um item no carrinho
- `DELETE /cart/{clientId}/items/{itemId}` - Remove um item do carrinho
- `DELETE /cart/{clientId}` - Limpa o carrinho

### Pedidos
- `GET /order` - Lista todos os pedidos
- `GET /order/{id}` - Obtém um pedido específico
- `GET /order/client/{clientId}` - Lista pedidos de um cliente
- `POST /order/{clientId}` - Adiciona item diretamente a um pedido
- `POST /order/{clientId}/checkout` - Cria um pedido a partir do carrinho
- `POST /order/{orderId}/payment` - Processa o pagamento de um pedido
- `PUT /order/{orderId}/status` - Atualiza o status de um pedido
- `DELETE /order/{id}` - Remove um pedido

## Documentação da API

A documentação completa da API está disponível em:
- `/swagger-ui.html` - Interface Swagger UI
