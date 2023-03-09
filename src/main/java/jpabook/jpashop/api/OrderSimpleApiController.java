package jpabook.jpashop.api;

import static java.util.stream.Collectors.toList;

import java.util.List;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.SimpleOrderDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import jpabook.jpashop.repository.order.simplequery.SimpleOrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

  private final OrderRepository orderRepository;
  private final OrderSimpleQueryRepository orderSimpleQueryRepository;

  @GetMapping("/api/v1/simple-orders")
  public List<Order> orderV1() {
    List<Order> all = orderRepository.findAllByString(new OrderSearch());
    for (Order order : all) {
      order.getMember().getName();
      order.getDelivery().getAddress();
    }
    return all;
  }

  @GetMapping("/api/v2/simple-orders")
  public List<SimpleOrderDto> orderV2() {
    return orderRepository.findAllByString(new OrderSearch())
        .stream()
        .map(SimpleOrderDto::new)
        .collect(toList());
  }

  @GetMapping("/api/v3/simple-orders")
  public List<SimpleOrderDto> orderV3() {
    return orderRepository.findAllWithMemberDelivery()
        .stream()
        .map(SimpleOrderDto::new)
        .collect(toList());
  }

  @GetMapping("/api/v4/simple-orders")
  public List<SimpleOrderQueryDto> orderV4() {
    return orderSimpleQueryRepository.findOrderDtos();
  }
}
