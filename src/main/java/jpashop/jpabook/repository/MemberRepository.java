package jpashop.jpabook.repository;

import jpashop.jpabook.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /*
     *  findBy~
     *  select m from Member m where m.name = ?
     */
    List<Member> findByName(String name);
}
