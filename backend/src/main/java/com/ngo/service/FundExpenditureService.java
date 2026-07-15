package com.ngo.service;
import com.ngo.dao.FundExpenditureDAO;
import com.ngo.model.FundExpenditure;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FundExpenditureService {
    private final FundExpenditureDAO dao = new FundExpenditureDAO();
    public boolean create(FundExpenditure fe) { return dao.create(fe); }
    public List<FundExpenditure> getAll() { return dao.getAll(); }
    public List<FundExpenditure> getByNgo(int ngoId) { return dao.getByNgo(ngoId); }
    public List<FundExpenditure> getPending() { return dao.getPending(); }
    public boolean approve(int id) { return dao.approve(id); }
    public boolean reject(int id) { return dao.reject(id); }
    public int getCount() { return dao.getCount(); }
}
