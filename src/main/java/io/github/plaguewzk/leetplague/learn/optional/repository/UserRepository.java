package io.github.plaguewzk.leetplague.learn.optional.repository;

import io.github.plaguewzk.leetplague.learn.optional.domain.User;

import java.util.Optional;

/**
 * 用户查询的外部接口。
 *
 * 这是一个可替换的适配器接缝：业务代码不需要知道用户来自 Map、数据库还是远程接口。
 */
public interface UserRepository {

    /**
     * 按编号查询用户。
     *
     * @param userId 用户编号
     * @return 找到用户时返回用户，否则返回 Optional.empty()
     */
    Optional<User> findById(String userId);
}
