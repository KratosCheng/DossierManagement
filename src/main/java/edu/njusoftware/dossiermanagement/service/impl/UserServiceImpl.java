package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.Role;
import edu.njusoftware.dossiermanagement.domain.UserDetail;
import edu.njusoftware.dossiermanagement.domain.User;
import edu.njusoftware.dossiermanagement.repository.RoleRepository;
import edu.njusoftware.dossiermanagement.repository.UserRepository;
import edu.njusoftware.dossiermanagement.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * 根据用户名获取该用户的所有信息， 包括用户信息和权限点
     * @param jobNum
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String jobNum) throws UsernameNotFoundException {
        User user = userRepository.findFirstByJobNum(jobNum);
        if (user == null) {
            return null;
        }

        UserDetail userDetail = new UserDetail(user.getJobNum(), user.getPassword(), user.getRoleName(), user.getStatus());
        List<Role> roleList = roleRepository.findAllByName(userDetail.getRoleName());
        userDetail.setAuthorities(roleList);
        return userDetail;
    }

    /**
     * 添加账户
     * @param user
     * @return
     */
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User modifyUserRole(String jobNum, String roleName) {
        User user = userRepository.findFirstByJobNum(jobNum);
        if (user == null) {
            return null;
        }
        user.setRoleName(roleName);
        return userRepository.save(user);
    }

    @Override
    public User getUserByJobNum(String jobNum) {
        return userRepository.findFirstByJobNum(jobNum);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllUsersByRoleName(String roleName) {
        return userRepository.findAllByRoleName(roleName);
    }
}
