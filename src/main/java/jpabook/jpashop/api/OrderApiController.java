package jpabook.jpashop.api;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository orderRepository;
  private final OrderQueryRepository orderQueryRepository;

  @GetMapping("/api/v1/orders")
  public List<Order> ordersV1() {
    List<Order> all = orderRepository.findAllByString(new OrderSearch());
    for (Order order : all) {
      order.getMember().getName();
      order.getDelivery().getAddress();
      List<OrderItem> orderItems = order.getOrderItems();
      orderItems.forEach(o -> o.getItem().getName());
    }

    return all;
  }

  @GetMapping("/api/v2/orders")
  public List<OrderDto> ordersV2() {
    List<Order> orders = orderRepository.findAllByString(new OrderSearch());
    return orders.stream().map(OrderDto::new)
        .collect(toList());
  }

  @GetMapping("/api/v3/orders")
  public List<OrderDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithItem();
    return orders.stream().map(OrderDto::new)
        .collect(toList());
  }

  @GetMapping("/api/v3.1/orders")
  public List<OrderDto> ordersV3_page(
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "100") int limit) {
    List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
    return orders.stream().map(OrderDto::new)
        .collect(toList());
  }

  @GetMapping("/api/v4/orders")
  public List<OrderQueryDto> ordersV4() {
    return orderQueryRepository.findOrderQueryDtos();
  }

  @GetMapping("/api/v5/orders")
  public List<OrderQueryDto> ordersV5() {
    return orderQueryRepository.findAllByDtoOptimization();
  }

  @GetMapping("/api/v6/orders")
  public List<OrderQueryDto> ordersV6() {
    List<OrderFlatDto> flats = orderQueryRepository.findAllByDtoFlat();
    return flats.stream()
        .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
                o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
            mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
        )).entrySet().stream()
        .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
            e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
            e.getKey().getAddress(), e.getValue()))
        .collect(toList());
  }

  @Getter
  static class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order o) {
      orderId = o.getId();
      name = o.getMember().getName();
      orderDate = o.getOrderDate();
      orderStatus = o.getStatus();
      address = o.getDelivery().getAddress();
      orderItems = o.getOrderItems().stream()
          .map(OrderItemDto::new)
          .collect(toList());
    }
  }

  @Getter
  static class OrderItemDto {

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemDto(OrderItem oi) {
      itemName = oi.getItem().getName();
      orderPrice = oi.getOrderPrice();
      count = oi.getCount();
    }
  }
}
