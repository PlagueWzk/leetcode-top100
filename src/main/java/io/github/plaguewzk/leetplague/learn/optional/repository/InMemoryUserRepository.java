package io.github.plaguewzk.leetplague.learn.optional.repository;

import io.github.plaguewzk.leetplague.learn.optional.domain.User;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Optional 练习使用的内存适配器。
 */
public final class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users;

    public InMemoryUserRepository(Map<String, User> users) {
        this.users = Map.copyOf(Objects.requireNonNull(users, "users"));
    }

    @Override
    public Optional<User> findById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }
}
