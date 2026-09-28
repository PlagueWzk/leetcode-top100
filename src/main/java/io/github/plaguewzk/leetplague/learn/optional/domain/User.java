package io.github.plaguewzk.leetplague.learn.optional.domain;

/**
 * 用户领域对象。领域对象本身不保存 Optional 字段。
 *
 * @param id       用户编号
 * @param username 用户名，允许为空或空白
 * @param nickname 昵称，允许为空或空白
 * @param profile  详细资料，允许为空
 */
public record User(String id, String username, String nickname, Profile profile) {
}
