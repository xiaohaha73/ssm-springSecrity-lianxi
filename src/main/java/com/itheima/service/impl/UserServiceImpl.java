package com.itheima.service.impl;

import com.itheima.dao.UserDao;
import com.itheima.domain.SysRole;
import com.itheima.domain.SysUser;
import com.itheima.service.RoleService;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    // 导入加密模块
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleService roleService;

    @Override
    public void save(SysUser user) {
        userDao.save(user);
    }

    @Override
    public List<SysUser> findAll() {
        return userDao.findAll();
    }

    @Override
    public Map<String, Object> toAddRolePage(Integer id) {
        List<SysRole> allRoles = roleService.findAll();
        List<Integer> myRoles = userDao.findRolesByUid(id);
        Map<String, Object> map = new HashMap<>();
        map.put("allRoles", allRoles);
        map.put("myRoles", myRoles);
        return map;
    }

    @Override
    public void addRoleToUser(Integer userId, Integer[] ids) {
        userDao.removeRoles(userId);
        for (Integer rid : ids) {
            userDao.addRoles(userId, rid);
        }
    }

    /**
     *
     * @param username 就是登陆表单的username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 调用dao层查询这个登陆用户是否存在
        SysUser sysUser = userDao.findByName(username);
        if (sysUser == null) {
            return null;
        }

        // 需要传递进User对象的角色列表
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // SimpleGrantedAuthority的角色值要和springSecurity配置文件中配置的角色名称匹配
        // authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // 从数据库中查询这个用户的所有用户角色，添加到authorities
        List<SysRole> roles = sysUser.getRoles();
        for (SysRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }


        // 将数据库中查询到的用户的username,password,和role列表传递进去
        // 如果数据库中的密码没有加密需要拼接 "{noop}"
        // UserDetails userDetails  = new User(username,"{noop}" + sysUser.getPassword(), authorities);
        // 使用加密模式的密码
        // UserDetails userDetails  = new User(username,sysUser.getPassword(), authorities);

        // 改写为增加一个用户是否处于禁用状态机制
        // 从数据库中获取用户的状态值status  sysUser.getStatus()
        boolean status = sysUser.getStatus() == 1;
        UserDetails userDetails = new User(username,sysUser.getPassword(),status,true,true,true,authorities);

        return userDetails;

    }
}
