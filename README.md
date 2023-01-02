# jpabook
jpa 학습


# 2022.12.25
- spring boot 기본 세팅
1) 타임리프 세팅
2) jpa 기본 환경 세팅
3) junit4 버전 관련 세팅

JAVA -> 객체   < - > 관계형 DB
-> 객체지향적 언어를 관계형 DB로 매핑 시키는 부분은 굉장히 부자연스러움
-> 객체지향적 언어와 관계형 DB 사이를 연결 시켜주는 기술(객체스럽게)

![image](https://user-images.githubusercontent.com/56577599/209471614-42cb1535-b25e-4fff-8cf6-2f58c2f50db2.png)



# 2022.12.26
- jpa entity 세팅

![image](https://user-images.githubusercontent.com/56577599/209555060-9387bd63-dab5-441c-b63a-a27025a1324b.png)

![image](https://user-images.githubusercontent.com/56577599/209555090-70646c22-40d6-49ef-84b5-ef53cb9cbd17.png)


1) 외래키가 존재하는 테이블을 연관관계의 주인으로 세팅(@JoinColumn)
2) 자동차, 자동차 바퀴(외래키)
3) 1대 1 관계에서는 더 많이 쓰는 테이블이 연관관계의 주인
4) 모든 연관관계는 지연 로딩으로 설계해야함(LAZY) (* 기본 : ~toOne(eager) ~toMany(lazy)
5) CasCadeType -> order 저장 시 OrderItem 도 같이 저장됨(cascade = CascadeType.ALL)
6) embedded embeddable -> 사용시 delivery 테이블에 address 클래스 필드값이 전부 추가됨




# 2022.12.28
- JPA 서비스 관련 개발 진행

1) 도메인 모델 패턴으로 개발
> 도메인 객체에 핵심 기능 적재


* INTELLIJ 단축키
-- ctrl alt n 메소드 합치기
-- crtl alt v 반환값 만들어주기
-- ctrl shift T -> 테이스 케이스 만들기
-- ctrl alt m -> 메소드 분리
-- ctrl alt p -> 매개변수로 빼기.. 
-- ctrl shift t -> 테스트 왔다리 갔다리




![image](https://user-images.githubusercontent.com/56577599/209759314-54e76ec5-a851-4ee0-8612-8daa76eded5f.png)



# 2022.12.29
- JPA 상품, 주문 관련 개발 진행



* 데이터를 업데이트 하는 방법 

1) merge 사용 (전체 컬럼을 정의해 줘야 하는 문제가 있음)
2) 변경감지(dirty check) 사용

![image](https://user-images.githubusercontent.com/56577599/209895623-97b1d6ed-dbaf-41f1-bf1f-5c7f857cf8ef.png)


EX) merge 사용
{ 
EntityManager em

Book book = new Book();
book.set~~

em.merge(book)
}

![image](https://user-images.githubusercontent.com/56577599/209895595-b17aae9c-b3dd-47a5-89b9-6818e0bea2b3.png)


EX) 변경 감지 사용
{
EntityManager em

Book book = new Book();
book.set~~

Book book = em.find~
book.set~~
}

![image](https://user-images.githubusercontent.com/56577599/209895572-2ca2dbfb-cf85-4751-9490-c6bcb845718c.png)


--> 엔티티를 영속성에 넣기 때문에 MERGE 또는 SAVE를 안하더라도 알아서 변경감지를 통해서 트랜잭션이 마무리 될때 업데이트를 함


>>>> 결론적으로 영속성으로 만드는 시점을 조회 할때 할거냐 MERGE 할때 할 것인가임



* 컨트롤러에서 엔티티를 불러오거나 생성하지 말것!
> 주문 등을 생성 시 TRANSCATION 안에 존재하면 변경 감지 등으로 회원 정보등도 업데이트 할 수 있음



# 2023.01.02

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/557f66cb-3a3e-4a15-9f31-35309e04e4b2/Untitled.png)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/87beefe4-7647-450b-9784-7b805ae2c53e/Untitled.png)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e68d9b8f-1bca-4082-ba92-15246c132617/Untitled.png)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5d2920ef-4162-43bd-a88d-2191ce5e1560/Untitled.png)

## 회원 API 관련 정리

### 회원 등록

V1

- 요청 값으로 **엔티티**를 직접 받는다.

```java
@PostMapping("/api/v1/members")
 public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member)
{
      Long id = memberService.join(member);
      return new CreateMemberResponse(id);
 }
```

1) 엔티티에 API 검증을 위한 로직 적재(@NotEmpty)

2) API 스펙이 엔티티에 의존적이게 됨

3) API 스펙에 따라서 DTO를 제작하여서 받고 반환해야함

V2

- 엔티티 대신에 **DTO**를 RequestBody에 매핑

```java
@PostMapping("/api/v2/members")
 public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
      Member member = new Member();
      member.setName(request.getName());
      Long id = memberService.join(member);
      return new CreateMemberResponse(id);
 }
```

1) 엔티티가 변해도 API 스펙이 변하지 않는다.

2) 엔티티와 API 스펙을 명확하게 분리할 수 있음

### 회원 수정

- DTO를 통해서 업데이트(변경 감지 사용)

```java
@PutMapping("/api/v2/members/{id}")
public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
@RequestBody @Valid UpdateMemberRequest request) {
      memberService.update(id, request.getName());
      Member findMember = memberService.findOne(id);
      return new UpdateMemberResponse(findMember.getId(), findMember.getName());
}

~~~~
@Transactional
 public void update(Long id, String name) {
 Member member = memberRepository.findOne(id);
 member.setName(name);
 }
```

1) DTO를 받아와서 변경 감지를 통해서 데이터를 변경

변경감지란 ?
→ memberRepository.findOne 을 통해서 영속성으로 엔티티를 가져오면 해당 객체를 수정하는 것만으로도 트랜잭션이 끝날 때 영속성 관리를 통해서 변경된 객체에 맞는 쿼리가 실행됨

### 회원 조회

V1

- 엔티티를 직접 조회

```java
@GetMapping("/api/v1/members")
 public List<Member> membersV1() {
      return memberService.findMembers();
 }

~~~~~~~~~~~~~

public List<Member> findMembers(){
        return memberRepository.findAll();
    }

~~~~~~~~~~~~~

public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
```

1) 기본적으로 엔티티의 모든값이 표현

2) 스펙을 맞추기 위한 @JsonIgnore 등이 사용해야 하는데 여러 api 스펙을 맞추기에 좋지 않음
> 엔티티를 수정하는 것이기 때문에

3) 엔티티가 변경되면 API 스펙이 변한다.

4) 컬렉션을 그대로 반환하면 좋지 않음( 값 등을 추가하기가 어려움)

```java
{
COUNT : 1,
[ ~~ ]
}
```

V2

- 응닶 값으로 엔티티가 아닌 별도의 DTO 사용

```java
@GetMapping("/api/v2/members")
 public Result membersV2() {
       List<Member> findMembers = memberService.findMembers();
 
      //엔티티 -> DTO 변환
       List<MemberDto> collect = findMembers.stream()
       .map(m -> new MemberDto(m.getName()))
       .collect(Collectors.toList());
       return new Result(collect);
 }

~~~~~~~~~~~~~~~

@Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

@Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T data;
    }
```

1) 엔티티를 DTO로 변환해서 반환한다.

2) 엔티티가 변해도 API 스펙이 변경되지 않는다.

3) Result 클래스로 컬렉션을 감싸서 나중에라도 필요한 필드를 추가할 수 있다.


주문 API 관련 정리
간단한 주문 조회(X TO ONE)
V1
엔티티를 직접 노출
@GetMapping("/api/v1/simple-orders")
 public List<Order> ordersV1() {
      List<Order> all = orderRepository.findAllByString(new OrderSearch());
      for (Order order : all) {
           order.getMember().getName(); //Lazy 강제 초기화
           order.getDelivery().getAddress(); //Lazy 강제 초기환
      }
      return all;
 }
1) 엔티티를 직접 노출하는 것은 좋지 않다.
2) ORDER → MEMBER, ORDER- > ADDRESS 는 지연 로딩이여서 실제 엔티티 대신 프록시 존재해서 에러 발생
→ jackson 라이브러리는 해당 방식을 몰라서 Hibernate5Module 을 스프링 빈으로 등록하며 해결
3) 엔티티를 직접 노출할 때는 한 곳을 @JsonIgnore 해야함
→ Member → Order, Order → Member 무한 동작
실행 쿼리
<!-- 주문조회 -->
select
        * 
    from
        ( select
            order0_.order_id as order_id1_6_,
            order0_.delivery_id as delivery_id4_6_,
            order0_.member_id as member_id5_6_,
            order0_.order_date as order_date2_6_,
            order0_.status as status3_6_ 
        from
            orders order0_ 
        inner join
            member member1_ 
                on order0_.member_id=member1_.member_id 
        where
            1=1 ) 
    where
        rownum <= ?

<!-- 주문에 따른 회원 & 배송 정보 조회 쿼리 2번 실행(주문이 2개) -->
select
        member0_.member_id as member_id1_4_0_,
        member0_.city as city2_4_0_,
        member0_.street as street3_4_0_,
        member0_.zipcode as zipcode4_4_0_,
        member0_.name as name5_4_0_ 
    from
        member member0_ 
    where
        member0_.member_id=?

select
        delivery0_.delivery_id as delivery_id1_2_0_,
        delivery0_.city as city2_2_0_,
        delivery0_.street as street3_2_0_,
        delivery0_.zipcode as zipcode4_2_0_,
        delivery0_.status as status5_2_0_ 
    from
        delivery delivery0_ 
    where
        delivery0_.delivery_id=?



select
        member0_.member_id as member_id1_4_0_,
        member0_.city as city2_4_0_,
        member0_.street as street3_4_0_,
        member0_.zipcode as zipcode4_4_0_,
        member0_.name as name5_4_0_ 
    from
        member member0_ 
    where
        member0_.member_id=?


select
        delivery0_.delivery_id as delivery_id1_2_0_,
        delivery0_.city as city2_2_0_,
        delivery0_.street as street3_2_0_,
        delivery0_.zipcode as zipcode4_2_0_,
        delivery0_.status as status5_2_0_ 
    from
        delivery delivery0_ 
    where
        delivery0_.delivery_id=?


<!-- orderItem 은 CasCade 여서 조회 되는 듯? -->
select
        orderitems0_.order_id as order_id5_5_0_,
        orderitems0_.order_item_id as order_item_id1_5_0_,
        orderitems0_.order_item_id as order_item_id1_5_1_,
        orderitems0_.count as count2_5_1_,
        orderitems0_.item_id as item_id4_5_1_,
        orderitems0_.order_id as order_id5_5_1_,
        orderitems0_.order_price as order_price3_5_1_ 
    from
        order_item orderitems0_ 
    where
        orderitems0_.order_id=?


select
        orderitems0_.order_id as order_id5_5_0_,
        orderitems0_.order_item_id as order_item_id1_5_0_,
        orderitems0_.order_item_id as order_item_id1_5_1_,
        orderitems0_.count as count2_5_1_,
        orderitems0_.item_id as item_id4_5_1_,
        orderitems0_.order_id as order_id5_5_1_,
        orderitems0_.order_price as order_price3_5_1_ 
    from
        order_item orderitems0_ 
    where
        orderitems0_.order_id=?
V2
엔티티를 DTO로 변환하는 일반적인 방법
@GetMapping("/api/v2/simple-orders")
public List<SimpleOrderDto> ordersV2() {
      List<Order> orders = orderRepository.findAll();
      List<SimpleOrderDto> result = orders.stream()
      .map(o -> new SimpleOrderDto(o))
      .collect(toList());
      return result;
}
1) 엔티티 대신 DTO를 반환하는 장점은 존재하나 쿼리 실행 수는 동일한다.
주문 → 1번(2개 나옴)
회원 → 2번
배달 → 2번
ORDERITEM → 2번
V3
엔티티를 DTO로 변환 & 페치 조인 최적화
@GetMapping("/api/v3/simple-orders")
public List<SimpleOrderDto> ordersV3() {
      List<Order> orders = orderRepository.findAllWithMemberDelivery();
      List<SimpleOrderDto> result = orders.stream()
      .map(o -> new SimpleOrderDto(o))
      .collect(toList());
      return result;
}

~~~

public List<Order> findAllWithMemberDelivery() {
 return em.createQuery(
 "select o from Order o" +
 " join fetch o.member m" +
 " join fetch o.delivery d", Order.class)
 .getResultList();
}


~~~

public OrderSimpleQueryDto(Order order){
        orderId = order.getId();
        name = order.getMember().getName(); //LAZY 초기화
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress(); //LAZY 초기화
    }

1) 지연로딩되서 여러번 쿼리가 실행 되는 것을 막기 위해서 전체를 조회한다.
실행 쿼리
select
        order0_.order_id as order_id1_6_0_,
        member1_.member_id as member_id1_4_1_,
        delivery2_.delivery_id as delivery_id1_2_2_,
        order0_.delivery_id as delivery_id4_6_0_,
        order0_.member_id as member_id5_6_0_,
        order0_.order_date as order_date2_6_0_,
        order0_.status as status3_6_0_,
        member1_.city as city2_4_1_,
        member1_.street as street3_4_1_,
        member1_.zipcode as zipcode4_4_1_,
        member1_.name as name5_4_1_,
        delivery2_.city as city2_2_2_,
        delivery2_.street as street3_2_2_,
        delivery2_.zipcode as zipcode4_2_2_,
        delivery2_.status as status5_2_2_ 
    from
        orders order0_ 
    inner join
        member member1_ 
            on order0_.member_id=member1_.member_id 
    inner join
        delivery delivery2_ 
            on order0_.delivery_id=delivery2_.delivery_id
V4
JPA 에서 ENTITY 를 직접 조회
@GetMapping("/api/v4/simple-orders")
     public List<OrderSimpleQueryDto> ordersV4() {
     return orderSimpleQueryRepository.findOrderDtos();
}


~~~~


public List<OrderSimpleQueryDto> findOrderDtos() {
 return em.createQuery(
 "select new 
jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, 
o.orderDate, o.status, d.address)" +
 " from Order o" +
 " join o.member m" +
 " join o.delivery d", OrderSimpleQueryDto.class)
 .getResultList();
 }


~~~~~

public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime 
orderDate, OrderStatus orderStatus, Address address) {
 this.orderId = orderId;
 this.name = name;
 this.orderDate = orderDate;
 this.orderStatus = orderStatus;
 this.address = address;
 }
1) 애플리케이션 네트웍 용량 최적화
 * 쿼리 방식 선택 권장 순서
우선 엔티티를 DTO로 변환하는 방법을 선택한다.
필요하면 페치 조인으로 성능을 최적화 한다. 대부분의 성능 이슈가 해결된다.
그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접
사용한다
복잡한 주문 조회(X TO MANY)
V1
엔티티를 직접 노출
@GetMapping("/api/v1/orders")
 public List<Order> ordersV1() {
      List<Order> all = orderRepository.findAllByString(new OrderSearch());
      for (Order order : all) {
           order.getMember().getName(); //Lazy 강제 초기화
           order.getDelivery().getAddress(); //Lazy 강제 초기환
           List<OrderItem> orderItems = order.getOrderItems();
           orderItems.stream().forEach(o -> o.getItem().getName()); //Lazy 강제초기화
 }
 return all;
 }
1) 위와 동일하지만 ORDERITEM 도 포함해서 조회
V2
엔티티를 DTO로 변환하는 일반적인 방법
@GetMapping("/api/v2/orders")
public List<OrderDto> ordersV2() {
      List<Order> orders = orderRepository.findAll();
      List<OrderDto> result = orders.stream()
      .map(o -> new OrderDto(o))
      .collect(toList());
      return result;
}


~~~~~

public OrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }

~~~~~~~

public OrderItemDto(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
1) 지연로딩으로 인해 많은 쿼리 진행
→ order 1번
→ member, address N번
→ orderItem N번
→ Item N번
V3
엔티티를 DTO로 변환 - 페치 조인 최적화
public List<OrderDto> ordersV3() {
      List<Order> orders = orderRepository.findAllWithItem();
      List<OrderDto> result = orders.stream()
      .map(o -> new OrderDto(o))
      .collect(toList());
      return result;
}

~~~~~~~~~

public List<Order> findAllWithItem() {
 return em.createQuery(
 "select distinct o from Order o" +
 " join fetch o.member m" +
 " join fetch o.delivery d" +
 " join fetch o.orderItems oi" +
 " join fetch oi.item i", Order.class)
 .getResultList();
}
1) 페치조인으로 쿼리가 한번만 실행됨
2) 1대 다 관계로 페이징 불가능(가능은 한데 메모리에 다올림)
3) 컬렉션 여러번 fetch join 불가능
 
 jpa3
V3.1
엔티티를 DTO로 변환 - 페이징과 한계돌파
@GetMapping("/api/v3.1/orders")
public List<OrderDto> ordersV3_page(@RequestParam(value = "offset",
defaultValue = "0") int offset,
 @RequestParam(value = "limit", defaultValue 
= "100") int limit) {
      List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,
      limit);
      List<OrderDto> result = orders.stream()
      .map(o -> new OrderDto(o))
      .collect(toList());
 return result;
}


~~~~

public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();
    }
1) x to one 만 fetch 조인해서 페이징 이후
2) to many 는 지연 로딩 처리(옵션으로 in 안에 쿼리 넣어서 최적화)
spring:
 jpa:
 properties:
 hibernate:
 default_batch_fetch_size: 1000
실행 쿼리
<!-- 주문 회원 배송 fetch join -->
select
        * 
    from
        ( select
            order0_.order_id as order_id1_6_0_,
            member1_.member_id as member_id1_4_1_,
            delivery2_.delivery_id as delivery_id1_2_2_,
            order0_.delivery_id as delivery_id4_6_0_,
            order0_.member_id as member_id5_6_0_,
            order0_.order_date as order_date2_6_0_,
            order0_.status as status3_6_0_,
            member1_.city as city2_4_1_,
            member1_.street as street3_4_1_,
            member1_.zipcode as zipcode4_4_1_,
            member1_.name as name5_4_1_,
            delivery2_.city as city2_2_2_,
            delivery2_.street as street3_2_2_,
            delivery2_.zipcode as zipcode4_2_2_,
            delivery2_.status as status5_2_2_ 
        from
            orders order0_ 
        inner join
            member member1_ 
                on order0_.member_id=member1_.member_id 
        inner join
            delivery delivery2_ 
                on order0_.delivery_id=delivery2_.delivery_id ) 
    where
        rownum <= ?


<!-- order_item in으로 한방에 조회 -->
select
        orderitems0_.order_id as order_id5_5_1_,
        orderitems0_.order_item_id as order_item_id1_5_1_,
        orderitems0_.order_item_id as order_item_id1_5_0_,
        orderitems0_.count as count2_5_0_,
        orderitems0_.item_id as item_id4_5_0_,
        orderitems0_.order_id as order_id5_5_0_,
        orderitems0_.order_price as order_price3_5_0_ 
    from
        order_item orderitems0_ 
    where
        orderitems0_.order_id in (
            ?, ?
        )

<!-- item in으로 한방에 조회 -->
select
        item0_.item_id as item_id2_3_0_,
        item0_.name as name3_3_0_,
        item0_.price as price4_3_0_,
        item0_.stock_quantity as stock_quantity5_3_0_,
        item0_.artist as artist6_3_0_,
        item0_.etc as etc7_3_0_,
        item0_.author as author8_3_0_,
        item0_.isbn as isbn9_3_0_,
        item0_.actor as actor10_3_0_,
        item0_.director as director11_3_0_,
        item0_.dtype as dtype1_3_0_ 
    from
        item item0_ 
    where
        item0_.item_id in (
            ?, ?, ?, ?
        )
V4
JPA에서 DTO를 직접 조회
@GetMapping("/api/v4/orders")
    List<OrderQueryDto> orderV4(
            @RequestParam(value ="offset", defaultValue = "0") int offset,
            @RequestParam(value ="limit", defaultValue = "100") int limit)
    {
        return orderQueryRepository.findOrderQueryDtos();
    }
}


~~~~~
public List<OrderQueryDto> findOrderQueryDtos() {
 //루트 조회(toOne 코드를 모두 한번에 조회)
      List<OrderQueryDto> result = findOrders();
 //루프를 돌면서 컬렉션 추가(추가 쿼리 실행)
       result.forEach(o -> {
       List<OrderItemQueryDto> orderItems =
       findOrderItems(o.getOrderId());
       o.setOrderItems(orderItems);
 });
       return result;
 }



private List<OrderQueryDto> findOrders() {
  return em.createQuery(
     "select new 
     jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, 
     o.status, d.address)" +
      " from Order o" +
      " join o.member m" +
      " join o.delivery d", OrderQueryDto.class)
      .getResultList();
 }

private List<OrderItemQueryDto> findOrderItems(Long orderId) {
      return em.createQuery(
       "select new 
      jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, 
      oi.orderPrice, oi.count)" +
       " from OrderItem oi" +
       " join oi.item i" +
       " where oi.order.id = : orderId",
      OrderItemQueryDto.class)
       .setParameter("orderId", orderId)
       .getResultList();
 }
1) DTO를 직접 조회 하고
2) 지연 로딩 처럼 ORDERITEM 이랑 ITEM을 따로 가져와서 조회한다.
V5
JPA에서 DTO를 직접 조회 -컬렉션 조회 최적화
@GetMapping("/api/v5/orders")
public List<OrderQueryDto> ordersV5() {
   return orderQueryRepository.findAllByDto_optimization();
}


~~~~~

public List<OrderQueryDto> findAllByDto_optimization() {
 //루트 조회(toOne 코드를 모두 한번에 조회)
      List<OrderQueryDto> result = findOrders();
 //orderItem 컬렉션을 MAP 한방에 조회
      Map<Long, List<OrderItemQueryDto>> orderItemMap =
     findOrderItemMap(toOrderIds(result));
 //루프를 돌면서 컬렉션 추가(추가 쿼리 실행X)
      result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
 return result;
}
private List<Long> toOrderIds(List<OrderQueryDto> result) {
     return result.stream()
     .map(o -> o.getOrderId())
    .collect(Collectors.toList());
}
private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long>
orderIds) {
     List<OrderItemQueryDto> orderItems = em.createQuery(
 "select new 
jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, 
oi.orderPrice, oi.count)" +
 " from OrderItem oi" +
 " join oi.item i" +
 " where oi.order.id in :orderIds", OrderItemQueryDto.class)
 .setParameter("orderIds", orderIds)
 .getResultList();
 return orderItems.stream()
 .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
1) 메모리에 담아서 한번에 JPA 쿼리 실행 시키는 정도..
V6
JPA에서 DTO로 직접 조회, 플랫 데이터 최적
@GetMapping("/api/v6/orders")
public List<OrderQueryDto> ordersV6() {
 List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
 return flats.stream()
   .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
   o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
    mapping(o -> new OrderItemQueryDto(o.getOrderId(),
   o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
   )).entrySet().stream()
   .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
  e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
  e.getKey().getAddress(), e.getValue()))
   .collect(toList());
}
1) 복잡하지만 결국은 JOIN 으로 한번에 가져와서 DISTINCT 처리를 메모리에서 한다는 뜻
2) 페이징 안되고
3) 메모리 부하도 걱정됨
4) 쿼리도 빠를 거라는 보장이 없음

