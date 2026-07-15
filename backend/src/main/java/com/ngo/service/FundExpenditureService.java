package com.ngo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ngo.dao.FundExpenditureDAO;
import com.ngo.model.FundExpenditure;

/*
 * Fund Expenditure Service
 */

@Service
public class FundExpenditureService {

    private FundExpenditureDAO dao = new FundExpenditureDAO();

    // Create expenditure
    public int create(FundExpenditure exp) {
        try {
            return dao.create(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Get all expenditures
    public List<FundExpenditure> getAll() {
        try {
            return dao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    // Get by ID
    public FundExpenditure getById(int id) {
        try {
            return dao.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get by NGO
    public List<FundExpenditure> getByNgo(int ngoId) {
        try {
            return dao.getByNgo(ngoId);
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    // Update expenditure
    public boolean update(FundExpenditure exp) {
        try {
            return dao.update(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete expenditure
    public boolean delete(int id) {
        try {
            return dao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
