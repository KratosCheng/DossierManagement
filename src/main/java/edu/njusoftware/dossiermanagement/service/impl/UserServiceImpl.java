package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.*;
import edu.njusoftware.dossiermanagement.domain.req.AccountOperationQueryCondition;
import edu.njusoftware.dossiermanagement.domain.req.AccountQueryCondition;
import edu.njusoftware.dossiermanagement.domain.req.RecordQueryCondition;
import edu.njusoftware.dossiermanagement.repository.DossierOperationRecordRepository;
import edu.njusoftware.dossiermanagement.repository.RoleRepository;
import edu.njusoftware.dossiermanagement.repository.UserOperationRecordRepository;
import edu.njusoftware.dossiermanagement.repository.UserRepository;
import edu.njusoftware.dossiermanagement.service.IUserService;
import edu.njusoftware.dossiermanagement.service.OperationRecordService;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DossierOperationRecordRepository dossierOperationRecordRepository;

    @Autowired
    private UserOperationRecordRepository userOperationRecordRepository;

    @Autowired
    private OperationRecordService operationRecordService;

    /**
     * 根据用户名获取该用户的所有信息， 包括用户信息和权限点
     * @param jobNum
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String jobNum) throws UsernameNotFoundException {
        Account user = userRepository.findFirstByJobNum(jobNum);
        if (user == null) {
            return null;
        }

        AccountDetail userDetail = new AccountDetail(user.getJobNum(), user.getPassword(), user.getRoleName(), user.getCreator(), user.getCreateTime());
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
    public Account addUser(Account user) {
        return userRepository.save(user);
    }

    @Override
    public Account modifyUserRole(String jobNum, String roleName) {
        Account user = userRepository.findFirstByJobNum(jobNum);
        if (user == null) {
            return null;
        }
        user.setRoleName(roleName);
        return userRepository.save(user);
    }

    @Override
    public Account getUserByJobNum(String jobNum) {
        return userRepository.findFirstByJobNum(jobNum);
    }

    @Override
    public List<Account> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Account> getAllUsersByRoleName(String roleName) {
        return userRepository.findAllByRoleName(roleName);
    }

    @Override
    public Page<DossierOperationRecord> getOperationRecords(RecordQueryCondition recordQueryCondition) {
        Pageable pageable = PageRequest.of(recordQueryCondition.getPageNum(), recordQueryCondition.getPageSize(),
                recordQueryCondition.isDescend() ? Sort.Direction.DESC : Sort.Direction.ASC, "operateTime");
        Page<DossierOperationRecord> page = dossierOperationRecordRepository.findAll(new Specification<DossierOperationRecord>(){
            @Override
            public Predicate toPredicate(Root<DossierOperationRecord> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (!StringUtils.isEmpty(recordQueryCondition.getOperation())) {
                    list.add(criteriaBuilder.equal(root.get("operation").as(String.class), recordQueryCondition.getOperation()));
                }

                if (!StringUtils.isEmpty(recordQueryCondition.getJobNum())) {
                    list.add(criteriaBuilder.equal(root.get("jobNum").as(String.class), recordQueryCondition.getJobNum()));
                }

                if (recordQueryCondition.getStatus() != 0) {
                    list.add(criteriaBuilder.equal(root.get("status").as(Integer.class), recordQueryCondition.getStatus()));
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
    public Page<Account> getUsers(AccountQueryCondition accountQueryCondition) {
        Pageable pageable = PageRequest.of(accountQueryCondition.getAccountPageNum(), accountQueryCondition.getAccountPageSize(),
                accountQueryCondition.isAccountDescend() ? Sort.Direction.DESC : Sort.Direction.ASC, "jobNum");
        Page<Account> page = userRepository.findAll(new Specification<Account>(){
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
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

    @Override
    public Page<UserOperationRecord> getUserOperationRecords(AccountOperationQueryCondition accountOperationQueryCondition) {
        Pageable pageable = PageRequest.of(accountOperationQueryCondition.getOperationPageNum(), accountOperationQueryCondition.getOperationPageSize(),
                accountOperationQueryCondition.isOperationDescend() ? Sort.Direction.DESC : Sort.Direction.ASC, "operateTime");
        Page<UserOperationRecord> page = userOperationRecordRepository.findAll(new Specification<UserOperationRecord>() {
            @Override
            public Predicate toPredicate(Root<UserOperationRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(accountOperationQueryCondition.getOperationType())) {
                    list.add(criteriaBuilder.equal(root.get("operation").as(String.class), accountOperationQueryCondition.getOperationType()));
                }

                // 工号模糊查询
                if (!StringUtils.isEmpty(accountOperationQueryCondition.getOperationKeyword())) {
                    String pattern = "%" + accountOperationQueryCondition.getOperationKeyword() + "%";
                    Predicate keywordPredicate = criteriaBuilder.or(
                            criteriaBuilder.like(root.get("jobNum").as(String.class), pattern),
                            criteriaBuilder.like(root.get("handler").as(String.class), pattern));
                    list.add(keywordPredicate);
                }
                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        }, pageable);
        return page;
    }

    @Transactional
    @Override
    public boolean saveUser(Account user) {
        Account oldUser = getUserByJobNum(user.getJobNum());
        if (oldUser == null) {
            // 添加用户
            user.setPassword(SecurityUtils.encodePassword(user.getPassword()));
            user.setCreator(SecurityUtils.getLoginUserName());
            user.setCreateTime(new Date());
            operationRecordService.saveAccountOperation(SecurityUtils.getLoginUserName(), user.getJobNum(), Constants.OPERATION_ADD);
        } else {
            // 修改用户
            user.setCreator(oldUser.getCreator());
            user.setCreateTime(oldUser.getCreateTime());
            // 修改了密码
            if (!oldUser.getPassword().equals(user.getPassword())) {
                user.setPassword(SecurityUtils.encodePassword(user.getPassword()));
                operationRecordService.saveUserModificationOperation(SecurityUtils.getLoginUserName(), user.getJobNum(),
                        Constants.OPERATION_MODIFY, "password", oldUser.getPassword(), user.getPassword());
            }
            // 修改了角色
            if (!oldUser.getRoleName().equals(user.getRoleName())) {
                operationRecordService.saveUserModificationOperation(SecurityUtils.getLoginUserName(), user.getJobNum(),
                        Constants.OPERATION_MODIFY, "role", oldUser.getRoleName(), user.getRoleName());
            }
        }
        logger.debug(SecurityUtils.getLoginUserName() + " updated user: " + user.getJobNum());
        return userRepository.save(user) != null;
    }

    /**
     * 删除用户
     * @param jobNum
     * @return
     */
    @Override
    public boolean removeUserByJobNum(String jobNum) {
        User user = userRepository.removeByJobNum(jobNum);
        if (user == null) {
            logger.error(SecurityUtils.getLoginUserName() + " tries to delete unknown user: " + jobNum);
            return false;
        }
        logger.debug(SecurityUtils.getLoginUserName() + " deleted user: " + jobNum);
        return true;
    }
}
