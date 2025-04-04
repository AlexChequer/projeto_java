package org.example.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemservice;

    @GetMapping("/item")
    public HashMap<Integer, Item> getItens() {
        return itemservice.getItens();
    }

    @GetMapping("/item/{id}")
    public Item getItem(@PathVariable int id) {
        return itemservice.getItem(id);
    }

    @PostMapping("/item")
    public String postItem(@RequestBody Item item) {
        itemservice.postItem(item);
        return "";
    }

    @PutMapping("/item/{id}")
    public String putItem(@PathVariable int id) {
        itemservice.putItem(id);
        return "";
    }

    @DeleteMapping("/item/{id}")
    public String deleteItem(@PathVariable int id) {
        itemservice.deleteItem(id);
        return "";
    }
}
