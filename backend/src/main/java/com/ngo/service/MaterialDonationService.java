package com.ngo.service;
import com.ngo.dao.MaterialDonationDAO;
import com.ngo.model.MaterialDonation;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaterialDonationService {
    private final MaterialDonationDAO dao = new MaterialDonationDAO();
    public boolean pledge(MaterialDonation md) { return dao.pledge(md); }
    public List<MaterialDonation> getAll() { return dao.getAll(); }
    public List<MaterialDonation> getByDonor(int donorId) { return dao.getByDonor(donorId); }
    public List<MaterialDonation> getByRequest(int requestId) { return dao.getByRequest(requestId); }
    public MaterialDonation getById(int id) { return dao.getById(id); }
    public boolean updateLogistics(int id, String courier, String tracking, String expectedDate) { return dao.updateLogistics(id, courier, tracking, expectedDate); }
    public boolean markDelivered(int id, int damagedQty) { return dao.markDelivered(id, damagedQty); }
    public int getCount() { return dao.getCount(); }
}
