package jpashop.jpabook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    //@JsonIgnore JSON 불러올 때 안가져오게 처리
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
