package org.example.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;

    public List<Item> getItens() {
        return itemRepository.findAll();
    }

    public Item getItem(int id) {
        return itemRepository.findById(id).
                orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Item postItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(int id) {
        itemRepository.deleteById(id);
    }

    public Item putItem(int id, Item item) {
        Item editItem = getItem(id);

        if (editItem != null) {
            if (item.getPrice() != 0) {
                editItem.setPrice(item.getPrice());
            }
            if (item.getStock() != 0) {
                editItem.setStock(item.getStock());
            }
            if (item.getName() !=null) {
                editItem.setName(item.getName());
            }

        }

        return itemRepository.save(editItem);
    }

}
