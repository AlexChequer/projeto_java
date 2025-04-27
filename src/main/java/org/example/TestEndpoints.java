package org.example;

import org.example.admin.Admin;
import org.example.client.Client;
import org.example.order.Order;
import org.example.stock.Category;
import org.example.stock.Item;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Profile("test")
public class TestEndpoints {

    private final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Bean
    public CommandLineRunner testAll() {
        return args -> {
            System.out.println("Iniciando testes de integração...");
            
            try {
                testCategoryEndpoints();
                
                testItemEndpoints();
                
                Client client = testClientEndpoints();
                
                testAdminEndpoints();
                
                testCartEndpoints(client.getId());
                
                testOrderEndpoints(client.getId());
                
                System.out.println("Todos os testes foram concluídos com sucesso!");
            } catch (Exception e) {
                System.err.println("Erro durante a execução dos testes: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        };
    }
    
    private void testCategoryEndpoints() {
        System.out.println("\n===== Testando endpoints de Categorias =====");
        
        String uniqueName = "Eletrônicos-" + System.currentTimeMillis();
        Category category = new Category();
        category.setName(uniqueName);
        
        ResponseEntity<Category> createResponse = restTemplate.postForEntity(
                BASE_URL + "/category", category, Category.class);
        
        System.out.println("Criando categoria: " + createResponse.getStatusCode());
        Category createdCategory = createResponse.getBody();
        assert createdCategory != null && createdCategory.getId() > 0;
        
        ResponseEntity<Category> getResponse = restTemplate.getForEntity(
                BASE_URL + "/category/" + createdCategory.getId(), Category.class);
        
        System.out.println("Recuperando categoria: " + getResponse.getStatusCode());
        Category retrievedCategory = getResponse.getBody();
        assert retrievedCategory != null && uniqueName.equals(retrievedCategory.getName());
        
        String updatedName = "Eletrônicos Atualizados-" + System.currentTimeMillis();
        retrievedCategory.setName(updatedName);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Category> requestEntity = new HttpEntity<>(retrievedCategory, headers);
        
        ResponseEntity<Category> updateResponse = restTemplate.exchange(
                BASE_URL + "/category/" + retrievedCategory.getId(), 
                HttpMethod.PUT, 
                requestEntity, 
                Category.class);
        
        System.out.println("Atualizando categoria: " + updateResponse.getStatusCode());
        Category updatedCategory = updateResponse.getBody();
        assert updatedCategory != null && updatedName.equals(updatedCategory.getName());
        
        ResponseEntity<Category[]> listResponse = restTemplate.getForEntity(
                BASE_URL + "/category", Category[].class);
        
        System.out.println("Listando categorias: " + listResponse.getStatusCode());
        Category[] categories = listResponse.getBody();
        assert categories != null && categories.length > 0;
        
        System.out.println("Testes de categoria concluídos com sucesso!");
    }
    
    private void testItemEndpoints() {
        System.out.println("\n===== Testando endpoints de Produtos =====");
        
        String categoryName = "Smartphones-" + System.currentTimeMillis();
        Category category = new Category();
        category.setName(categoryName);
        
        ResponseEntity<Category> categoryResponse = restTemplate.postForEntity(
                BASE_URL + "/category", category, Category.class);
        
        Category createdCategory = categoryResponse.getBody();
        assert createdCategory != null;
        
        String productName = "Smartphone XYZ-" + System.currentTimeMillis();
        Item item = new Item();
        item.setName(productName);
        item.setPrice(999.99);
        item.setStock(50);
        item.setDescription("Um smartphone incrível!");
        item.setCategory(createdCategory);
        
        ResponseEntity<Item> createResponse = restTemplate.postForEntity(
                BASE_URL + "/item", item, Item.class);
        
        System.out.println("Criando produto: " + createResponse.getStatusCode());
        Item createdItem = createResponse.getBody();
        assert createdItem != null && createdItem.getId() > 0;
        
        ResponseEntity<Item> getResponse = restTemplate.getForEntity(
                BASE_URL + "/item/" + createdItem.getId(), Item.class);
        
        System.out.println("Recuperando produto: " + getResponse.getStatusCode());
        Item retrievedItem = getResponse.getBody();
        assert retrievedItem != null && productName.equals(retrievedItem.getName());
        
        retrievedItem.setPrice(899.99);
        retrievedItem.setStock(45);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Item> requestEntity = new HttpEntity<>(retrievedItem, headers);
        
        ResponseEntity<Item> updateResponse = restTemplate.exchange(
                BASE_URL + "/item/" + retrievedItem.getId(), 
                HttpMethod.PUT, 
                requestEntity, 
                Item.class);
        
        System.out.println("Atualizando produto: " + updateResponse.getStatusCode());
        Item updatedItem = updateResponse.getBody();
        assert updatedItem != null && updatedItem.getPrice() == 899.99;
        
        ResponseEntity<Item[]> categoryItemsResponse = restTemplate.getForEntity(
                BASE_URL + "/item/category/" + createdCategory.getId(), Item[].class);
        
        System.out.println("Buscando produtos por categoria: " + categoryItemsResponse.getStatusCode());
        Item[] itemsByCategory = categoryItemsResponse.getBody();
        assert itemsByCategory != null && itemsByCategory.length > 0;
        
        System.out.println("Testes de produto concluídos com sucesso!");
    }
    
    private Client testClientEndpoints() {
        System.out.println("\n===== Testando endpoints de Cliente =====");
        
        Client client = new Client(0, "João Silva", "joao@exemplo.com", "senha123");
        
        ResponseEntity<Client> createResponse = restTemplate.postForEntity(
                BASE_URL + "/client", client, Client.class);
        
        System.out.println("Criando cliente: " + createResponse.getStatusCode());
        Client createdClient = createResponse.getBody();
        assert createdClient != null && createdClient.getId() > 0;
        
        ResponseEntity<Client> getResponse = restTemplate.getForEntity(
                BASE_URL + "/client/" + createdClient.getId(), Client.class);
        
        System.out.println("Recuperando cliente: " + getResponse.getStatusCode());
        Client retrievedClient = getResponse.getBody();
        assert retrievedClient != null && "João Silva".equals(retrievedClient.getName());
        
        retrievedClient.setName("João da Silva");
        retrievedClient.setEmail("joao.silva@exemplo.com");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Client> requestEntity = new HttpEntity<>(retrievedClient, headers);
        
        ResponseEntity<Client> updateResponse = restTemplate.exchange(
                BASE_URL + "/client/" + retrievedClient.getId(), 
                HttpMethod.PUT, 
                requestEntity, 
                Client.class);
        
        System.out.println("Atualizando cliente: " + updateResponse.getStatusCode());
        Client updatedClient = updateResponse.getBody();
        assert updatedClient != null && "João da Silva".equals(updatedClient.getName());
        
        ResponseEntity<Client[]> listResponse = restTemplate.getForEntity(
                BASE_URL + "/client", Client[].class);
        
        System.out.println("Listando clientes: " + listResponse.getStatusCode());
        Client[] clients = listResponse.getBody();
        assert clients != null && clients.length > 0;
        
        System.out.println("Testes de cliente concluídos com sucesso!");
        return updatedClient;
    }
    
    private void testAdminEndpoints() {
        System.out.println("\n===== Testando endpoints de Admin =====");
        
        Admin admin = new Admin(0, "Admin Master", "admin@exemplo.com", "admin123");
        
        ResponseEntity<Admin> createResponse = restTemplate.postForEntity(
                BASE_URL + "/admin", admin, Admin.class);
        
        System.out.println("Criando admin: " + createResponse.getStatusCode());
        Admin createdAdmin = createResponse.getBody();
        assert createdAdmin != null && createdAdmin.getId() > 0;
        
        ResponseEntity<Admin> getResponse = restTemplate.getForEntity(
                BASE_URL + "/admin/" + createdAdmin.getId(), Admin.class);
        
        System.out.println("Recuperando admin: " + getResponse.getStatusCode());
        Admin retrievedAdmin = getResponse.getBody();
        assert retrievedAdmin != null && "Admin Master".equals(retrievedAdmin.getName());
        
        retrievedAdmin.setName("Super Admin");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Admin> requestEntity = new HttpEntity<>(retrievedAdmin, headers);
        
        ResponseEntity<Admin> updateResponse = restTemplate.exchange(
                BASE_URL + "/admin/" + retrievedAdmin.getId(), 
                HttpMethod.PUT, 
                requestEntity, 
                Admin.class);
        
        System.out.println("Atualizando admin: " + updateResponse.getStatusCode());
        Admin updatedAdmin = updateResponse.getBody();
        assert updatedAdmin != null && "Super Admin".equals(updatedAdmin.getName());
        
        System.out.println("Testes de admin concluídos com sucesso!");
    }
    
    private void testCartEndpoints(int clientId) {
        System.out.println("\n===== Testando endpoints de Carrinho =====");
        
        Item item = new Item();
        item.setName("Produto para Carrinho");
        item.setPrice(50.0);
        item.setStock(20);
        
        ResponseEntity<Item> itemResponse = restTemplate.postForEntity(
                BASE_URL + "/item", item, Item.class);
        
        Item createdItem = itemResponse.getBody();
        assert createdItem != null;
        
        Map<String, Object> addRequest = new HashMap<>();
        addRequest.put("itemId", createdItem.getId());
        addRequest.put("quantity", 2);
        
        ResponseEntity<Map> addResponse = restTemplate.postForEntity(
                BASE_URL + "/cart/" + clientId + "/items", 
                addRequest, 
                Map.class);
        
        System.out.println("Adicionando item ao carrinho: " + addResponse.getStatusCode());
        assert addResponse.getStatusCode().is2xxSuccessful();
        
        ResponseEntity<Map> getCartResponse = restTemplate.getForEntity(
                BASE_URL + "/cart/" + clientId, Map.class);
        
        System.out.println("Verificando o carrinho: " + getCartResponse.getStatusCode());
        Map<String, Object> cart = getCartResponse.getBody();
        assert cart != null;
        
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("quantity", 3);
        
        ResponseEntity<Map> updateResponse = restTemplate.exchange(
                BASE_URL + "/cart/" + clientId + "/items/" + createdItem.getId(), 
                HttpMethod.PUT, 
                new HttpEntity<>(updateRequest, new HttpHeaders()), 
                Map.class);
        
        System.out.println("Atualizando item no carrinho: " + updateResponse.getStatusCode());
        assert updateResponse.getStatusCode().is2xxSuccessful();
        
        ResponseEntity<Map> clearResponse = restTemplate.exchange(
                BASE_URL + "/cart/" + clientId, 
                HttpMethod.DELETE, 
                null, 
                Map.class);
        
        System.out.println("Limpando o carrinho: " + clearResponse.getStatusCode());
        assert clearResponse.getStatusCode().is2xxSuccessful();
        
        System.out.println("Testes de carrinho concluídos com sucesso!");
    }
    
    private void testOrderEndpoints(int clientId) {
        System.out.println("\n===== Testando endpoints de Pedido =====");
        
        ResponseEntity<Client> clientResponse = restTemplate.getForEntity(
                BASE_URL + "/client/" + clientId, Client.class);
        System.out.println("Verificando cliente: " + clientResponse.getStatusCode());
        Client client = clientResponse.getBody();
        assert client != null;
        
        ResponseEntity<Map> clearResponse = restTemplate.exchange(
                BASE_URL + "/cart/" + clientId, 
                HttpMethod.DELETE, 
                null, 
                Map.class);
        System.out.println("Limpando o carrinho para início do teste: " + clearResponse.getStatusCode());
        
        Item item = new Item();
        item.setName("Produto para Pedido - " + System.currentTimeMillis());
        item.setPrice(100.0);
        item.setStock(10);
        
        ResponseEntity<Item> itemResponse = restTemplate.postForEntity(
                BASE_URL + "/item", item, Item.class);
        
        Item createdItem = itemResponse.getBody();
        assert createdItem != null;
        System.out.println("Item criado com ID: " + createdItem.getId());
        
        Map<String, Object> addRequest = new HashMap<>();
        addRequest.put("itemId", createdItem.getId());
        addRequest.put("quantity", 2);
        
        ResponseEntity<Map> addToCartResponse = restTemplate.postForEntity(
                BASE_URL + "/cart/" + clientId + "/items", 
                addRequest, 
                Map.class);
        
        System.out.println("Adicionando item ao carrinho para o pedido: " + addToCartResponse.getStatusCode());
        assert addToCartResponse.getStatusCode().is2xxSuccessful();
        
        ResponseEntity<Map> getCartResponse = restTemplate.getForEntity(
                BASE_URL + "/cart/" + clientId, Map.class);
        
        System.out.println("Verificando conteúdo do carrinho: " + getCartResponse.getStatusCode());
        Map<String, Object> cart = getCartResponse.getBody();
        assert cart != null;
        List<?> items = (List<?>) cart.get("items");
        System.out.println("Itens no carrinho: " + (items != null ? items.size() : 0));
        assert items != null && !items.isEmpty();
        
        try {
            ResponseEntity<Order> checkoutResponse = restTemplate.postForEntity(
                    BASE_URL + "/order/" + clientId + "/checkout", 
                    null, 
                    Order.class);
            
            System.out.println("Criando pedido a partir do carrinho: " + checkoutResponse.getStatusCode());
            Order createdOrder = checkoutResponse.getBody();
            assert createdOrder != null && createdOrder.getId() > 0;
            System.out.println("Pedido criado com ID: " + createdOrder.getId());
            
            Map<String, Object> paymentRequest = new HashMap<>();
            paymentRequest.put("paymentType", "CREDIT_CARD");
            paymentRequest.put("amount", 200.0f);
            
            ResponseEntity<Map> paymentResponse = restTemplate.postForEntity(
                    BASE_URL + "/order/" + createdOrder.getId() + "/payment", 
                    paymentRequest, 
                    Map.class);
            
            System.out.println("Processando pagamento: " + paymentResponse.getStatusCode());
            assert paymentResponse.getStatusCode().is2xxSuccessful();
            
            Map<String, String> statusRequest = new HashMap<>();
            statusRequest.put("status", "SHIPPED");
            
            ResponseEntity<Order> statusResponse = restTemplate.exchange(
                    BASE_URL + "/order/" + createdOrder.getId() + "/status", 
                    HttpMethod.PUT, 
                    new HttpEntity<>(statusRequest, new HttpHeaders()), 
                    Order.class);
            
            System.out.println("Atualizando status do pedido: " + statusResponse.getStatusCode());
            Order updatedOrder = statusResponse.getBody();
            assert updatedOrder != null && "SHIPPED".equals(updatedOrder.getStatus());
            
            ResponseEntity<Order[]> clientOrdersResponse = restTemplate.getForEntity(
                    BASE_URL + "/order/client/" + clientId, 
                    Order[].class);
            
            System.out.println("Listando pedidos do cliente: " + clientOrdersResponse.getStatusCode());
            Order[] clientOrders = clientOrdersResponse.getBody();
            assert clientOrders != null && clientOrders.length > 0;
        } catch (Exception e) {
            System.err.println("Erro durante o teste de pedidos: " + e.getMessage());
            e.printStackTrace();
            throw e; 
        }
        
        System.out.println("Testes de pedido concluídos com sucesso!");
    }
}