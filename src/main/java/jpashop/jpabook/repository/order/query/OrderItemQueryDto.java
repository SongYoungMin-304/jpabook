package jpashop.jpabook.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrderItemQueryDto {

    @JsonIgnore
    private Long orderId;
    private String itemNmae;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long orderId, String itemNmae, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemNmae = itemNmae;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
