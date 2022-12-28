package jpashop.jpabook.domain.item;

import jpashop.jpabook.domain.Category;
import jpashop.jpabook.exception.NotEnoughStockEception;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Setter @Getter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name ="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany
    @JoinTable(name ="category_item",
            joinColumns = @JoinColumn(name ="category_id"),
            inverseJoinColumns = @JoinColumn(name ="item_id"))
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직

    /*
      stock 증가
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /*
      stock 감소
     */
    public void removeStock(int quantity){
        int resStock = this.stockQuantity - quantity;
        if(resStock < 0){
            throw new NotEnoughStockEception("need more stock");
        }
        this.stockQuantity = resStock;
    }
}
