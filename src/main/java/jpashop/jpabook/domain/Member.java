package jpashop.jpabook.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name ="member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // mappey By 주인 값이 아님
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
