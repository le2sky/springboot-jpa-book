package jpabook.jpashop.repository.order.simplequery;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderSimpleQueryRepository {

  private final EntityManager em;

  public List<SimpleOrderQueryDto> findOrderDtos() {
    return em.createQuery(
        "select new jpabook.jpashop.repository.SimpleOrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) "
            + "from Order o "
            + "join o.member m "
            + "join o.delivery d", SimpleOrderQueryDto.class
    ).getResultList();
  }
}
