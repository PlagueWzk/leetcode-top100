package io.github.plaguewzk.leetplague.learn.optional.domain;

/**
 * 用户地址。
 *
 * @param city 城市，允许为空或空白，用于练习 Optional.filter
 */
public record Address(String city) {
}
