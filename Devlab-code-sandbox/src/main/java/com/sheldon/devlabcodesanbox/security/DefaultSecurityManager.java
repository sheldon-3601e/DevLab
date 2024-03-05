package com.sheldon.devlabcodesanbox.security;

import java.security.Permission;

/**
 * @author sheldon
 * @version 1.0
 * @className DefaultSecurityManager
 * @date 2024/3/6 0:27
 * @description 默认安全管理器
 */
public class DefaultSecurityManager extends SecurityManager{

    @Override
    public void checkPermission(Permission perm) {
        System.out.println("检查权限：" + perm);
        throw new SecurityException("权限异常:" + perm);
//        super.checkPermission(perm);
    }



}
