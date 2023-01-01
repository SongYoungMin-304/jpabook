package jpashop.jpabook.api;

import jpashop.jpabook.domain.Order;
import jpashop.jpabook.repository.OrderRepository;
import jpashop.jpabook.repository.OrderSearch;
import jpashop.jpabook.repository.order.simplequery.OrderSimpleQueryDto;
import jpashop.jpabook.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/*
 * x to One -> many to one, one to one
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for(Order order : all){
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress();
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleQueryDto> orderV2(){
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        // Order 2개 조회
        // 1 + 회원 N + 배송 N  .. 쿼리 5개....
        List<OrderSimpleQueryDto> result = orders.stream()
                .map(o -> new OrderSimpleQueryDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleQueryDto> orderV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        List<OrderSimpleQueryDto> result = orders.stream()
                .map(o -> new OrderSimpleQueryDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4(){
        return orderSimpleQueryRepository.findOrderDtos();
    }
}
