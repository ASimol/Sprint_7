package org.example;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.List;


public class OrderGenerator {
    public static Order newOrder(List<String> color) {
        Order order = new Order();
        order.setFirstName(RandomStringUtils.randomAlphabetic(10));
        order.setLastName(RandomStringUtils.randomAlphabetic(10));
        order.setAddress(RandomStringUtils.randomAlphabetic(20));
        order.setPhone(RandomStringUtils.randomAlphabetic(11));
        order.setDeliveryDate(LocalDate.now());
        order.setComment(RandomStringUtils.randomAlphabetic(100));
        order.setRentTime((int)(Math.random() * (10)));
        order.setColor(color);
        return order;
    }
}
