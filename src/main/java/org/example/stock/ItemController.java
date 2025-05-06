package org.example.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.dto.ItemCreateDTO;
import org.example.stock.exception.CategoryNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Item>> getItens() {
        return ResponseEntity.ok(itemService.getItens());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable int id) {
        return ResponseEntity.ok(itemService.getItem(id));
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Item>> getItemsByCategory(@PathVariable int categoryId) {
        return ResponseEntity.ok(itemService.getItemsByCategory(categoryId));
    }

    @PostMapping
    public ResponseEntity<Item> postItem(@RequestBody ItemCreateDTO item) {
        Item newItem = new Item();
        newItem.setName(item.getName());
        newItem.setDescription(item.getDescription());
        newItem.setPrice(item.getPrice());
        newItem.setStock(item.getStock());
        newItem.setImageUrl(item.getImageUrl());

        // Associando a categoria ao item
        if (item.getCategoryId() > 0) {
            try {
                Category category = categoryService.getCategoryById(item.getCategoryId());
                newItem.setCategory(category);
            } catch (Exception e) {
                // Caso a categoria não seja encontrada, continue sem associar
                System.out.println("Categoria não encontrada: " + e.getMessage());
            }
        }
        
        Item createdItem = itemService.postItem(newItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> putItem(@PathVariable int id, @RequestBody Item item) {
        Item updatedItem = itemService.putItem(id, item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable int id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
