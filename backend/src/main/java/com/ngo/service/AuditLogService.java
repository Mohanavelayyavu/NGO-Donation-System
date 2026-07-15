package com.ngo.service;
import com.ngo.dao.AuditLogDAO;
import com.ngo.model.AuditLog;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuditLogService {
    private final AuditLogDAO dao = new AuditLogDAO();
    public boolean log(int adminId, String action, String details, int targetId, String targetType) { return dao.log(adminId, action, details, targetId, targetType); }
    public List<AuditLog> getAll() { return dao.getAll(); }
    public List<AuditLog> getRecent(int limit) { return dao.getRecent(limit); }
}
