package jpashop.jpabook.domain.item;

import jpashop.jpabook.domain.Category;
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

}
