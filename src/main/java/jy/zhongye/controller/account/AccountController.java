package jy.zhongye.controller.account;

import jy.zhongye.dao.account.AccountDao;
import jy.zhongye.entity.account.Permission;
import jy.zhongye.entity.account.Role;
import jy.zhongye.entity.account.User;
import jy.zhongye.entity.common.Result;
import jy.zhongye.entity.common.ResultEnum;
import jy.zhongye.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;


    /***
     * @method listUsers
     * @description list all users and user's roles
     * @author
     * @date
     * @param
     * @return
     */
    @RequestMapping("/listUsers")
    public Result listUsers() {

        //返回所有用户并获取用户拥有角色
        List<User> users = accountService.selectAllUser();
        for (User u : users) {
            List<Role> s = accountService.listRoles(u);
            u.setRoles(s);
        }

        return new Result(ResultEnum.SUCCESS, users);
    }

    /***
     * @method listRoles
     * @description list all roles and role's permission
     * @author
     * @date
     * @param
     * @return
     */
    @RequestMapping("/listRoles")
    public Result listRoles() {

        List<Role> roles = accountService.selectAllRole();
        for (Role r : roles) {
            List<Permission> p = accountService.listPermissionsByRole(r);
            r.setPermissions(p);
        }

        return new Result(ResultEnum.SUCCESS, roles);
    }

    /***
     * @method listPermissions
     * @description list all permissions
     * @author
     * @date
     * @param
     * @return
     */
    @RequestMapping("/listPermissions")
    public Result listPermissions() {

        List<Permission> permissions = accountService.selectAllPermission();

        return new Result(ResultEnum.SUCCESS, permissions);
    }

    /**
     * @Description:add user
     * @Param: []
     * @return: java.lang.String
     * @Author: Mr.Xuyu
     * @Date: 2019-01-10
     */
    @RequestMapping("/addUser")
    public Result addUser(String username, String password, String[] rolesTem) {

        //创建角色对象
        List<Role> roles = new ArrayList<Role>();
        for (String r : rolesTem) {
            Role role = accountService.getRoleByRoleName(r);
            roles.add(role);
        }

        //添加用户并添加权限
        return accountService.addUser(username, password, roles);
    }

    /**
     * @Description:增加权限
     * @Param: [reg, response]
     * @return: jy.zhongye.entity.common.Result
     * @Author: Mr.Xuyu
     * @Date: 2019-01-24
     */
    @RequestMapping("/addRole")
    public Result addRole(String roleName, String roleDesc, String[] permissionsTem) {

        //创建权限对象
        List<Permission> permissions = new ArrayList<Permission>();
        for (String p : permissionsTem) {
            Permission permission = accountService.getPermissionByPermissionName(p);
            permissions.add(permission);
        }

        //添加权限给角色
        return accountService.addRole(roleName, roleDesc, permissions);
    }

    /**
     * @Description:增加权限
     * @Param: [reg, response]
     * @return: jy.zhongye.entity.common.Result
     * @Author: Mr.Xuyu
     * @Date: 2019-01-24
     */
    @RequestMapping("/addPermission")
    public Result addPermission(String permissionName, String permissionDesc) {

        //添加权限给角色
        return accountService.addPermission(permissionName, permissionDesc);
    }

    /**
     * @Description:用户更新角色
     * @Param: [username, rolesName]
     * @return: jy.zhongye.entity.common.Result
     * @Author: Mr.Xuyu
     * @Date: 2019-01-25
     */
    @RequestMapping("/userUpdateRoles")
    public Result userUpdateRoles(String username, List<String> rolesName) {

        User user = accountService.getUserByUsername(username);

        //获得所有角色实体
        List<Role> roles = new ArrayList<>();
        for (String role : rolesName) {
            roles.add(accountService.getRoleByRoleName(role));
        }

        return accountService.userUpdateRoles(user, roles);
    }

    /**
     * @Description:角色更新权限
     * @Param: [roleName, permissions]
     * @return: jy.zhongye.entity.common.Result
     * @Author: Mr.Xuyu
     * @Date: 2019-02-12
     */
    @RequestMapping("/roleUpdatePermissions")
    public Result roleUpdatePermissions(String roleName, List<String> permissions) {

        Role role = accountService.getRoleByRoleName(roleName);

        //获得所有角色实体
        List<Permission> permissionsTemp = new ArrayList<>();
        for (String permission : permissions) {
            permissionsTemp.add(accountService.getPermissionByPermissionName(permission));
        }

        return accountService.roleUpdatePerminssions(role, permissionsTemp);
    }

}
