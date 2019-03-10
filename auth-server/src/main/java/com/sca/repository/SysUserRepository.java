package com.sca.repository;

import com.sca.domain.SysUser;
import com.sca.repository.support.WiselyRepository;

import java.util.Optional;

/**
 * Created by wangyunfei on 2017/6/9.
 */
public interface SysUserRepository extends WiselyRepository<SysUser,Long> {
    Optional<SysUser> findOneWithRolesByUsername(String username);
}
