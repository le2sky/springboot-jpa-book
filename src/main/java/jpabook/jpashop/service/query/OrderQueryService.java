package jpabook.jpashop.service.query;

import static java.util.stream.Collectors.toList;

import java.util.List;
import jpabook.jpashop.api.OrderApiController.OrderDto;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderQueryService {

  private final OrderRepository orderRepository;

  public List<OrderDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithItem();
    return orders.stream().map(OrderDto::new)
        .collect(toList());
  }
}
