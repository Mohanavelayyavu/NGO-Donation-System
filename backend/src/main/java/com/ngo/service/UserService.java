package com.ngo.service;
import com.ngo.dao.UserDAO;
import com.ngo.model.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserDAO dao = new UserDAO();
    public boolean registerUser(User u) { return dao.registerUser(u); }
    public User loginUser(String email, String password, String role) { return dao.loginUser(email, password, role); }
    public List<User> getAllUsers() { return dao.getAllUsers(); }
    public User getUserById(int id) { return dao.getUserById(id); }
    public List<User> getUsersByRole(String role) { return dao.getUsersByRole(role); }
    public boolean updateUser(User u) { return dao.updateUser(u); }
    public boolean deleteUser(int id) { return dao.deleteUser(id); }
    public int getTotalCount() { return dao.getTotalCount(); }
    public int getCountByRole(String role) { return dao.getCountByRole(role); }
}
