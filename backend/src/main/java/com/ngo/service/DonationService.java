package com.ngo.service;
import com.ngo.dao.DonationDAO;
import com.ngo.model.Donation;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DonationService {
    private final DonationDAO dao = new DonationDAO();
    public boolean create(Donation d) { return dao.create(d); }
    public List<Donation> getAll() { return dao.getAll(); }
    public List<Donation> getByDonor(int donorId) { return dao.getByDonor(donorId); }
    public List<Donation> getByRequest(int requestId) { return dao.getByRequest(requestId); }
    public Donation getById(int id) { return dao.getById(id); }
    public double getTotalAmount() { return dao.getTotalAmount(); }
    public int getCount() { return dao.getCount(); }
    public double getTotalAmountByDonor(int donorId) { return dao.getTotalAmountByDonor(donorId); }
}
