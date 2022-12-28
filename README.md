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



