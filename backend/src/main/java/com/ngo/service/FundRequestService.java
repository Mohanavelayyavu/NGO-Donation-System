package com.ngo.service;
import com.ngo.dao.FundRequestDAO;
import com.ngo.model.FundRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FundRequestService {
    private final FundRequestDAO dao = new FundRequestDAO();
    public boolean create(FundRequest fr) { return dao.create(fr); }
    public List<FundRequest> getAll() { return dao.getAll(); }
    public List<FundRequest> getOpen() { return dao.getOpen(); }
    public List<FundRequest> getByNgo(int ngoId) { return dao.getByNgo(ngoId); }
    public FundRequest getById(int id) { return dao.getById(id); }
    public boolean updateCollectedAmount(int requestId, double amount) { return dao.updateCollectedAmount(requestId, amount); }
    public boolean updateStatus(int id, String status) { return dao.updateStatus(id, status); }
    public int getCount() { return dao.getCount(); }
    public int getCountByStatus(String status) { return dao.getCountByStatus(status); }
}
