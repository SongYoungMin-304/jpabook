# jpabook
jpa 학습


2022.12.25
- spring boot 기본 세팅
1) 타임리프 세팅
2) jpa 기본 환경 세팅
3) junit4 버전 관련 세팅

JAVA -> 객체   < - > 관계형 DB
-> 객체지향적 언어를 관계형 DB로 매핑 시키는 부분은 굉장히 부자연스러움
-> 객체지향적 언어와 관계형 DB 사이를 연결 시켜주는 기술(객체스럽게)

![image](https://user-images.githubusercontent.com/56577599/209471614-42cb1535-b25e-4fff-8cf6-2f58c2f50db2.png)



2022.12.26
- jpa entity 세팅

![image](https://user-images.githubusercontent.com/56577599/209555060-9387bd63-dab5-441c-b63a-a27025a1324b.png)

![image](https://user-images.githubusercontent.com/56577599/209555090-70646c22-40d6-49ef-84b5-ef53cb9cbd17.png)


1) 외래키가 존재하는 테이블을 연관관계의 주인으로 세팅(@JoinColumn)
2) 자동차, 자동차 바퀴(외래키)
3) 1대 1 관계에서는 더 많이 쓰는 테이블이 연관관계의 주인
4) 모든 연관관계는 지연 로딩으로 설계해야함(LAZY) (* 기본 : ~toOne(eager) ~toMany(lazy)
5) CasCadeType -> order 저장 시 OrderItem 도 같이 저장됨(cascade = CascadeType.ALL)
6) embedded embeddable -> 사용시 delivery 테이블에 address 클래스 필드값이 전부 추가됨




2022.12.28
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



2022.12.29
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



2023.01.02

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










