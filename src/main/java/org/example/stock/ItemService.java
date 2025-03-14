package org.example.stock;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ItemService {
    private HashMap<Integer, Item> stock;

    public HashMap<Integer, Item> getItens() {
        return stock;
    }

    public Item getItem(int id) {
        for (Item item: stock.values()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void postItem(Item item) {
        stock.put(item.getId(), item);
    }

    public void deleteItem(int id) {
        for (Item v: stock.values()) {
            if (id == v.getId()) {
                stock.remove(v);
            }
        }
    }

    public void putItem(int id) {

    }

}
