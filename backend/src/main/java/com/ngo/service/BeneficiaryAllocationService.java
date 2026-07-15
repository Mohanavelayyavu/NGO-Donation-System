package com.ngo.service;
import com.ngo.dao.BeneficiaryAllocationDAO;
import com.ngo.model.BeneficiaryAllocation;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BeneficiaryAllocationService {
    private final BeneficiaryAllocationDAO dao = new BeneficiaryAllocationDAO();
    public boolean create(BeneficiaryAllocation ba) { return dao.create(ba); }
    public List<BeneficiaryAllocation> getAll() { return dao.getAll(); }
    public List<BeneficiaryAllocation> getByNgo(int ngoId) { return dao.getByNgo(ngoId); }
    public List<BeneficiaryAllocation> getByBeneficiaryRequest(int requestId) { return dao.getByBeneficiaryRequest(requestId); }
    public boolean confirmReceipt(int id) { return dao.confirmReceipt(id); }
    public int getCount() { return dao.getCount(); }
}
