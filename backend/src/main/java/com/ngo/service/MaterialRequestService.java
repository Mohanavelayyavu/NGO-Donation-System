package com.ngo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.ngo.dao.MaterialRequestDAO;
import com.ngo.model.MaterialRequest;

/*
 * Material Request Service
 */
@Service
public class MaterialRequestService {

    private MaterialRequestDAO dao = new MaterialRequestDAO();

    public int create(MaterialRequest req) {
        try { return dao.create(req); } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public List<MaterialRequest> getAll() {
        try { return dao.getAll(); } catch (Exception e) { e.printStackTrace(); return new java.util.ArrayList<>(); }
    }

    public List<MaterialRequest> getOpen() {
        try { return dao.getOpen(); } catch (Exception e) { e.printStackTrace(); return new java.util.ArrayList<>(); }
    }

    public List<MaterialRequest> getByNgo(int ngoId) {
        try { return dao.getByNgo(ngoId); } catch (Exception e) { e.printStackTrace(); return new java.util.ArrayList<>(); }
    }

    public List<MaterialRequest> getByBeneficiary(int beneficiaryId) {
        try { return dao.getByBeneficiary(beneficiaryId); } catch (Exception e) { e.printStackTrace(); return new java.util.ArrayList<>(); }
    }

    public boolean fulfill(int requestId, int quantity, int donorId, String itemName) {
        try { return dao.fulfill(requestId, quantity, donorId, itemName); } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateStatus(int id, String status) {
        try { return dao.updateStatus(id, status); } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean delete(int id) {
        try { return dao.delete(id); } catch (Exception e) { e.printStackTrace(); return false; }
    }
}
