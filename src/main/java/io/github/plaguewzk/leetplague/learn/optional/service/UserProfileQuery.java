package io.github.plaguewzk.leetplague.learn.optional.service;

import java.util.Optional;

/**
 * 用户资料查询模块的接口。
 */
public interface UserProfileQuery {

    Optional<String> findCity(String userId);

    String getDisplayName(String userId);

    Optional<String> findValidEmail(String userId);
}
