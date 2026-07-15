package com.ngo.service;
import com.ngo.dao.BeneficiaryDAO;
import com.ngo.model.Beneficiary;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BeneficiaryService {
    private final BeneficiaryDAO dao = new BeneficiaryDAO();
    public boolean create(Beneficiary b) { return dao.create(b); }
    public List<Beneficiary> getPending() { return dao.getPending(); }
    public List<Beneficiary> getAll() { return dao.getAll(); }
    public List<Beneficiary> getByNgo(int ngoId) { return dao.getByNgo(ngoId); }
    public boolean verify(int id) { return dao.verify(id); }
    public boolean reject(int id) { return dao.reject(id); }
    public Beneficiary getById(int id) { return dao.getById(id); }
    public int getCount() { return dao.getCount(); }
}
