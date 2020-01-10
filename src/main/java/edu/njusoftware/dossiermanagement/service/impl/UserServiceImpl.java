package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.*;
import edu.njusoftware.dossiermanagement.domain.req.AccountQueryCondition;
import edu.njusoftware.dossiermanagement.domain.req.RecordQueryCondition;
import edu.njusoftware.dossiermanagement.repository.OperationRecordRepository;
import edu.njusoftware.dossiermanagement.repository.RoleRepository;
import edu.njusoftware.dossiermanagement.repository.UserRepository;
import edu.njusoftware.dossiermanagement.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OperationRecordRepository operationRecordRepository;

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

    @Override
    public Page<OperationRecord> getOperationRecords(RecordQueryCondition recordQueryCondition) {
        Pageable pageable = PageRequest.of(recordQueryCondition.getPageNum(), recordQueryCondition.getPageSize(),
                recordQueryCondition.isDescend() ? Sort.Direction.DESC : Sort.Direction.ASC, "operateTime");
        Page<OperationRecord> page = operationRecordRepository.findAll(new Specification<OperationRecord>(){
            @Override
            public Predicate toPredicate(Root<OperationRecord> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (!StringUtils.isEmpty(recordQueryCondition.getOperation())) {
                    list.add(criteriaBuilder.equal(root.get("operation").as(String.class), recordQueryCondition.getOperation()));
                }

                if (!StringUtils.isEmpty(recordQueryCondition.getJobNum())) {
                    list.add(criteriaBuilder.equal(root.get("jobNum").as(String.class), recordQueryCondition.getJobNum()));
                }

                // 案号案由模糊查询
                if (!StringUtils.isEmpty(recordQueryCondition.getKeyword())) {
                    String pattern = "%" + recordQueryCondition.getKeyword() + "%";
                    Predicate keywordPredicate =
                            criteriaBuilder.or(criteriaBuilder.like(root.get("jobNum").as(String.class), pattern),
                                    criteriaBuilder.like(root.get("caseNum").as(String.class), pattern),
                                    criteriaBuilder.like(root.get("dossierName").as(String.class), pattern));
                    list.add(keywordPredicate);
                }
                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        }, pageable);
        return page;
    }

    @Override
    public Page<User> getUsers(AccountQueryCondition accountQueryCondition) {
        Pageable pageable = PageRequest.of(accountQueryCondition.getAccountPageNum(), accountQueryCondition.getAccountPageSize(),
                accountQueryCondition.isAccountDescend() ? Sort.Direction.DESC : Sort.Direction.ASC, "jobNum");
        Page<User> page = userRepository.findAll(new Specification<User>(){
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (!StringUtils.isEmpty(accountQueryCondition.getRole())) {
                    list.add(criteriaBuilder.equal(root.get("roleName").as(String.class), accountQueryCondition.getRole()));
                }

                // 工号模糊查询
                if (!StringUtils.isEmpty(accountQueryCondition.getAccountKeyword())) {
                    String pattern = "%" + accountQueryCondition.getAccountKeyword() + "%";
                    Predicate keywordPredicate = criteriaBuilder.like(root.get("jobNum").as(String.class), pattern);
                    list.add(keywordPredicate);
                }
                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        }, pageable);
        return page;
    }
}
