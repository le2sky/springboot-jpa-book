package jpabook.jpashop;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

  private final InitService initService;

  @PostConstruct
  public void init() {
    initService.dbInit1();
    initService.dbInit2();
  }

  @Component
  @Transactional
  @RequiredArgsConstructor
  static class InitService {

    private final EntityManager em;

    public void dbInit1() {
      Member member = createUser("userA", new Address("서울", "1", "1111"));
      Book book = createBook("JPA book1", 10000, 100);
      Book book2 = createBook("JPA book2", 20000, 100);
      OrderItem orderItem1 = OrderItem.createOrderItem(book, 10000, 1);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
      Delivery delivery = createDelivery(member);
      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
      em.persist(order);
    }

    public void dbInit2() {
      Member member = createUser("userB", new Address("진주", "2", "1121"));
      Book book = createBook("Spring book1", 10000, 100);
      Book book2 = createBook("Spring book2", 20000, 100);
      OrderItem orderItem1 = OrderItem.createOrderItem(book, 20000, 3);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);
      Delivery delivery = createDelivery(member);
      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
      em.persist(order);
    }

    private Book createBook(String name, int price, int stockQuantity) {
      Book book = new Book();
      book.setName(name);
      book.setPrice(price);
      book.setStockQuantity(stockQuantity);
      em.persist(book);
      return book;
    }

    private  Delivery createDelivery(Member member) {
      Delivery delivery = new Delivery();
      delivery.setAddress(member.getAddress());
      return delivery;
    }

    private Member createUser(String username, Address address) {
      Member member = new Member();
      member.setName(username);
      member.setAddress(address);
      em.persist(member);
      return member;
    }
  }
}
