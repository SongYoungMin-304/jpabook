package jpashop.jpabook.repository;

import jpashop.jpabook.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    private String memberName; //회원리음
    private OrderStatus orderStatus; //주문 상태[ORDER, CANCEL]

}
