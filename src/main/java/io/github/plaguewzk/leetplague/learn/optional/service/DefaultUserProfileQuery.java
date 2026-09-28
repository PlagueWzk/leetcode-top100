package io.github.plaguewzk.leetplague.learn.optional.service;

import io.github.plaguewzk.leetplague.learn.optional.domain.Address;
import io.github.plaguewzk.leetplague.learn.optional.domain.Profile;
import io.github.plaguewzk.leetplague.learn.optional.domain.User;
import io.github.plaguewzk.leetplague.learn.optional.repository.UserRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * UserProfileQuery 的默认实现。
 * <p>
 * 练习要求：只在这里完成用户资料的 Optional 链式查询，不修改接口。
 */
public final class DefaultUserProfileQuery implements UserProfileQuery {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final UserRepository userRepository;

    public DefaultUserProfileQuery(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository");
    }

    @Override
    public Optional<String> findCity(String userId) {
        return getUser(userId).map(User::profile).map(Profile::address).map(Address::city).map(String::trim).filter(
                s -> !s.isBlank());
    }

    @Override
    public String getDisplayName(String userId) {
        Optional<User> user = getUser(userId);
        return user.flatMap(u -> normalizeNonBlank(u.nickname()).or(() -> normalizeNonBlank(u.username()))).orElse(
                "匿名用户");
    }

    @Override
    public Optional<String> findValidEmail(String userId) {
        return getUser(userId).map(User::profile)
                .map(Profile::email)
                .flatMap(this::normalizeNonBlank)
                .filter(this::isEmail);
    }

    private Optional<User> getUser(String userId) {
        return Optional.ofNullable(userId).flatMap(this::normalizeNonBlank).flatMap(userRepository::findById);
    }

    private Optional<String> normalizeNonBlank(String value) {
        return Optional.ofNullable(value).map(String::trim).filter(s -> !s.isBlank());
    }

    private boolean isEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
