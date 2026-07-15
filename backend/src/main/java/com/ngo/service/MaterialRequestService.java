package com.ngo.service;
import com.ngo.dao.MaterialRequestDAO;
import com.ngo.model.MaterialRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaterialRequestService {
    private final MaterialRequestDAO dao = new MaterialRequestDAO();
    public boolean create(MaterialRequest mr) { return dao.create(mr); }
    public List<MaterialRequest> getAll() { return dao.getAll(); }
    public List<MaterialRequest> getOpen() { return dao.getOpen(); }
    public List<MaterialRequest> getByNgo(int ngoId) { return dao.getByNgo(ngoId); }
    public MaterialRequest getById(int id) { return dao.getById(id); }
    public boolean updateReceivedQty(int id, int qty) { return dao.updateReceivedQty(id, qty); }
    public boolean updateStatus(int id, String status) { return dao.updateStatus(id, status); }
    public int getCount() { return dao.getCount(); }
}
