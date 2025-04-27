package org.example.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Item> getItens() {
        return itemRepository.findAll();
    }

    public Item getItem(int id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    public Item postItem(Item item) {
        if (item.getCategory() != null && item.getCategory().getId() > 0) {
            Category category = categoryRepository.findById(item.getCategory().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));
            item.setCategory(category);
        }
        return itemRepository.save(item);
    }

    public void deleteItem(int id) {
        if (!itemRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
        itemRepository.deleteById(id);
    }

    public Item putItem(int id, Item item) {
        Item existingItem = getItem(id);

        if (item.getPrice() > 0) {
            existingItem.setPrice(item.getPrice());
        }
        if (item.getStock() >= 0) {
            existingItem.setStock(item.getStock());
        }
        if (item.getName() != null && !item.getName().trim().isEmpty()) {
            existingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            existingItem.setDescription(item.getDescription());
        }
        if (item.getImageUrl() != null) {
            existingItem.setImageUrl(item.getImageUrl());
        }
        if (item.getCategory() != null && item.getCategory().getId() > 0) {
            Category category = categoryRepository.findById(item.getCategory().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));
            existingItem.setCategory(category);
        }

        return itemRepository.save(existingItem);
    }
    
    public List<Item> getItemsByCategory(int categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
                
        return itemRepository.findAll().stream()
                .filter(item -> item.getCategory() != null && 
                        item.getCategory().getId() == categoryId)
                .toList();
    }
}
