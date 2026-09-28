package io.github.plaguewzk.leetplague.learn.collection;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/** 集合练习入口；题目和例子见同目录 README.md。 */
public final class CollectionExercises {

    private CollectionExercises() {}

    public static Map<String, List<String>> groupOrderIdsByDepartment(List<Order> orders) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        if (orders == null) {
            return result;
        }
        return orders.stream()
                .collect(
                        Collectors.groupingBy(
                                Order::department,
                                LinkedHashMap::new,
                                Collectors.mapping((order) -> order.orderId, Collectors.toList())));
    }

    public static Map<String, BigDecimal> totalPaidByDepartment(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return new LinkedHashMap<>();
        }
        int i = new Random().nextInt(3);
        switch (i) {
            case 0 -> {
                List<Order> list = orders.stream().filter(Order::paid).toList();
                Map<String, BigDecimal> result = new LinkedHashMap<>();
                for (Order order : list) {
                    result.merge(order.department, order.amount, BigDecimal::add);
                }
                return result;
            }
            case 1 -> {
                List<Order> list = orders.stream().filter(Order::paid).toList();
                Map<String, BigDecimal> result = new LinkedHashMap<>();
                for (Order order : list) {
                    BigDecimal bigDecimal = result.get(order.department);
                    if (bigDecimal == null) {
                        bigDecimal = BigDecimal.ZERO;
                    }
                    bigDecimal = bigDecimal.add(order.amount);
                    result.put(order.department, bigDecimal);
                }
                return result;
            }
            case 2 -> {
                return orders.stream()
                        .filter(Order::paid)
                        .collect(
                                Collectors.groupingBy(
                                        Order::department,
                                        LinkedHashMap::new,
                                        Collectors.reducing(
                                                BigDecimal.ZERO,
                                                (order) -> order.amount,
                                                BigDecimal::add)));
            }
        }
        return null;
    }

    public static List<String> distinctSkus(List<String> skus) {
        return skus.stream().distinct().toList();
    }

    public static Map<String, List<String>> snapshotGroups(Map<String, List<String>> groups) {
        if (groups == null) {
            return new LinkedHashMap<>();
        }
        Map<String, List<String>> result = new LinkedHashMap<>();
        groups.forEach((key, value) -> result.put(key, List.of(value.toArray(new String[0]))));
        return Collections.unmodifiableMap(result);
    }

    public record Order(String department, String orderId, BigDecimal amount, boolean paid) {}
}
