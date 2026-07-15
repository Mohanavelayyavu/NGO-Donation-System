package com.ngo.service;
import com.ngo.dao.BeneficiaryRequestDAO;
import com.ngo.model.BeneficiaryRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BeneficiaryRequestService {
    private final BeneficiaryRequestDAO dao = new BeneficiaryRequestDAO();
    public boolean create(BeneficiaryRequest br) { return dao.create(br); }
    public List<BeneficiaryRequest> getAll() { return dao.getAll(); }
    public List<BeneficiaryRequest> getByBeneficiary(int beneficiaryId) { return dao.getByBeneficiary(beneficiaryId); }
    public List<BeneficiaryRequest> getPending() { return dao.getPending(); }
    public boolean approve(int id) { return dao.approve(id); }
    public int getCount() { return dao.getCount(); }
}
