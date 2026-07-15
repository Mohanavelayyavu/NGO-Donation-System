package com.ngo.service;
import com.ngo.dao.NgoInventoryDAO;
import com.ngo.model.NgoInventory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NgoInventoryService {
    private final NgoInventoryDAO dao = new NgoInventoryDAO();
    public List<NgoInventory> getByNgo(int ngoId) { return dao.getByNgo(ngoId); }
    public List<NgoInventory> getAll() { return dao.getAll(); }
    public boolean addStock(int ngoId, String itemName, String category, int qty) { return dao.addStock(ngoId, itemName, category, qty); }
    public boolean removeStock(int ngoId, String itemName, int qty) { return dao.removeStock(ngoId, itemName, qty); }
    public int getCount() { return dao.getCount(); }
}
