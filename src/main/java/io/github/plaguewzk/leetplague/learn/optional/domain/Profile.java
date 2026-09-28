package io.github.plaguewzk.leetplague.learn.optional.domain;

/**
 * 用户资料。
 *
 * @param address 地址，允许为空
 * @param email   邮箱，允许为空或空白
 */
public record Profile(Address address, String email) {
}
