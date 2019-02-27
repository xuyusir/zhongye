package jy.zhongye.service.account.accountserviceimpl;

import jy.zhongye.dao.account.AccountDao;
import jy.zhongye.entity.account.Permission;
import jy.zhongye.entity.account.Role;
import jy.zhongye.entity.account.User;
import jy.zhongye.entity.common.Result;
import jy.zhongye.entity.common.ResultEnum;
import jy.zhongye.service.account.AccountService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service("AccountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountDao accountDao;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated
     */
    public List<User> selectAllUser() {
        return accountDao.selectAllUser();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated
     */
    public List<Role> selectAllRole() {
        return accountDao.selectAllRole();
    }

//    User-Role

    /***
     * @method getPassword
     * @description
     * @author
     * @date
     * @param        username
     * @return
     */
    @Override
    public String getPassword(String username) {
        return accountDao.getPassword(username);
    }

    /***
     * @method listRoles
     * @description
     * @author
     * @date
     * @param        user
     * @return
     */
    @Override
    public List<Role> listRoles(User user) {
        return accountDao.listRoles(user);
    }

    /***
     * @method listPermissions
     * @description
     * @author
     * @date
     * @param        user
     * @return
     */
    @Override
    public List<Permission> listPermissions(User user) {
        return accountDao.listPermissions(user);
    }

    /***
     * @method listPermissionByRole
     * @description
     * @author
     * @date
     * @param        role
     * @return
     */
    public List<Permission> listPermissionsByRole(Role role) {
        return accountDao.listPermissionsByRole(role);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated
     */
    public List<Permission> selectAllPermission() {
        return accountDao.selectAllPermission();
    }

    /**
     * @Description:
     * @Param: [username]
     * @return: java.lang.Long
     * @Author: Mr.Xuyu
     * @Date: 2019-01-10
     */
    public User getUserByUsername(String username) {
        return accountDao.getUserByUsername(username);
    }

    /**
     * @Description:
     * @Param: [roleName]
     * @return: java.lang.Long
     * @Author: Mr.Xuyu
     * @Date: 2019-01-10
     */
    public Role getRoleByRoleName(String roleName) {
        return accountDao.getRoleByRoleName(roleName);
    }

    /**
     * @Description:
     * @Param: [permissionName]
     * @return: java.lang.Long
     * @Author: Mr.Xuyu
     * @Date: 2019-01-10
     */
    public Permission getPermissionByPermissionName(String permissionName) {
        return accountDao.getPermissionByPermissionName(permissionName);
    }

    //以下是对用户-角色-权限的一些操作

    /**
     * @Description:add user
     * @Param: [username, password, roles]
     * @return: Result
     * @Author: Mr.Xuyu
     * @Date: 2019-01-10
     */
    @Transactional
    public Result addUser(String username, String password, List<Role> roles) throws DataAccessException {

        //判断是否有同名，如有，返回失败
        List<User> users=accountDao.selectAllUser();
        for (User user:users){
            if (user.getUsername().equals(username)){
                return new Result(ResultEnum.FAIL, null);
            }
        }

        User userTem = new User();
        userTem.setUsername(username);
        userTem.setPassword(password);

        //add user dao
        if (accountDao.insertUser(userTem) != 1) {
            return new Result(ResultEnum.FAIL, null);
        }

        //get user id dao
        User user = accountDao.getUserByUsername(userTem.getUsername());

        //add user roles
        for (Role r : roles) {
            if (accountDao.userAddRole(user, r) != 1) {
                return new Result(ResultEnum.FAIL, null);
            }
        }

        //success
        return new Result(ResultEnum.SUCCESS, null);
    }

    /**
     * @Description:add role
     * @Param: [username, password, roles]
     * @return: Result
     * @Author: Mr.Xuyu
     * @Date: 2019-01-10
     */
    @Transactional
    public Result addRole(String roleName, String roleDesc, List<Permission> permissions) {

        //判断是否有同名，如有，返回失败
        List<Role> roles=accountDao.selectAllRole();
        for (Role role:roles){
            if (role.getRoleName().equals(roleName)){
                return new Result(ResultEnum.FAIL, null);
            }
        }

        Role roleTem = new Role();
        roleTem.setRoleName(roleName);
        roleTem.setRoleDesc(roleDesc);

        //insert role
        if (accountDao.insertRole(roleTem) != 1) {
            return new Result(ResultEnum.FAIL, null);
        }

        Role Role = accountDao.getRoleByRoleName(roleTem.getRoleName());

        for (Permission p : permissions) {
            if (accountDao.roleAddPermissions(Role, p) != 1) {
                return new Result(ResultEnum.FAIL, null);
            }
        }

        //success
        return new Result(ResultEnum.SUCCESS, null);
    }

    /**
     * @Description:添加权限
     * @Param: [permissionName, permissionDesc]
     * @return: jy.zhongye.entity.common.Result
     * @Author: Mr.Xuyu
     * @Date: 2019-01-24
     */
    @Transactional
    public Result addPermission(String permissionName, String permissionDesc) {

        //判断是否有同名，如有，返回失败
        List<Permission> permissions=accountDao.selectAllPermission();
        for (Permission permission:permissions){
            if (permission.getPermissionName().equals(permissionName)){
                return new Result(ResultEnum.FAIL, null);
            }
        }

        Permission permission = new Permission();
        permission.setPermissionName(permissionName);
        permission.setPermissionDesc(permissionDesc);

        if (accountDao.insertPermission(permission) != 1) {
            return new Result(ResultEnum.FAIL, null);
        }

        //success
        return new Result(ResultEnum.SUCCESS, null);
    }

    /**
     * @Description:给一个用户更新重置角色
     * @Param: [user, roles]
     * @return: jy.zhongye.entity.common.Result
     * @Author: Mr.Xuyu
     * @Date: 2019-01-24
     */
    @Transactional
    public Result userUpdateRoles(User user, List<Role> roles) {

        //添加与删除flag
        int flag1 = 0;
        int flag2 = 0;

        //双重循环，如果用户没有新角色则添加此角色
        for (Role role : roles) {
            for (Role userRole : user.getRoles()) {
                if (userRole.getRoleName().equals(role.getRoleName())) {
                    flag1 = 1;
                }
            }

            if (flag1 == 1) {
                accountDao.userAddRole(user, role);
                flag1 = 0;
            }
        }

        //双重循环，如果用户有在新角色没有则删除此角色
        for (Role userRole : user.getRoles()) {
            flag2 = 1;
            for (Role role : roles) {
                if (userRole.getRoleName() == role.getRoleName()) {
                    flag2 = 0;
                }
            }

            if (flag2 == 1) {
                accountDao.userDeleteRole(user, userRole);
            }
        }

        return new Result(ResultEnum.SUCCESS, null);

    }

    /**
     * @Description:给一个角色更新重置权限
     * @Param: [user, roles]
     * @return: jy.zhongye.entity.common.Result
     * @Author: Mr.Xuyu
     * @Date: 2019-01-24
     */
    @Transactional
    public Result roleUpdatePerminssions(Role role, List<Permission> permissions) {

        //添加与删除flag
        int flag1 = 0;
        int flag2 = 0;

        //双重循环，如果角色没有新权限则添加此权限
        for (Permission permission : permissions) {
            for (Permission rolePermission : role.getPermissions()) {
                if (rolePermission.getPermissionName().equals(permission.getPermissionName())) {
                    flag1 = 1;
                }
            }

            if (flag1 == 1) {
                accountDao.roleAddPermissions(role, permission);
                flag1 = 0;
            }
        }

        //双重循环，如果角色有此权限而新权限里面没有此权限删除此权限
        for (Permission rolePermission : role.getPermissions()) {
            flag2=1;
            for (Permission permission : permissions) {
                if (rolePermission.getPermissionName().equals(permission.getPermissionName())) {
                    flag1 = 0;
                }
            }

            if (flag1 == 1) {
                accountDao.roleDeletePermission(role, rolePermission);
            }
        }

        return new Result(ResultEnum.SUCCESS, null);

    }

}