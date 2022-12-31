package jpashop.jpabook;

import jpashop.jpabook.domain.*;
import jpashop.jpabook.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


/*
 * 총 2주문
 * * userA
 * * * JPA1 BOOK
 * * * JPA2 BOOK
 * * userB
 * * * SPRING1 BOOK
 * * * SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        public void dbInit1(){
            Member member = getMember("userA", "서울");

            Book book1 = getBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);

            Book book2 = getBook("JPA2 BOOK", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddres(member.getAddress());

            Order order = getOrder(member, orderItem1, orderItem2, delivery);
            em.persist(order);
        }

        public void dbInit2(){
            Member member = getMember("userB", "부산");

            Book book1 = getBook("SPRING1 BOOK", 20000, 200);
            em.persist(book1);

            Book book2 = getBook("SPRING2 BOOK", 40000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddres(member.getAddress());

            Order order = getOrder(member, orderItem1, orderItem2, delivery);
            em.persist(order);
        }

        private Order getOrder(Member member, OrderItem orderItem1, OrderItem orderItem2, Delivery delivery) {
            return Order.createOrder(member, delivery, orderItem1, orderItem2);
        }

        private Book getBook(String s, int i, int q) {
            Book book1 = new Book();
            book1.setName(s);
            book1.setPrice(i);
            book1.setStockQuantity(q);
            return book1;
        }

        private Member getMember(String userA, String 서울) {
            Member member = new Member();
            member.setName(userA);
            member.setAddress(new Address(서울, "1", "11"));
            em.persist(member);
            return member;
        }
    }

}

