# DCOS - AP(Auto Provisioning)
Java web application 으로 Cloud 제품별 Excel 혹은 Script 내용을 기반으로 자동을 AWS/GCP Resources 생성, 변경하는 웹플랫폼

## 주기능
 - Excel 혹은 Script 에 기입한 생성/변경 정보를 읽어드린다. (Reader)
 - 정보를 객체화하고 DB 에 이력을 남긴다.
   - 진행 상태를 확인할 수 있게 진행 단계를 함께 남긴다. 
 - Cloud product type 에 따라 SDK 이용해 request 를 보낸다. (Sender)
 
## 환경
 - Java 11
 - Spring-boot-gradle.2.1.5.RELEASE
 - JPA 2.1.5.RELEASE
   - Hikari
 - MariaDB 10.3
 - Undertow
 - Zipkin & Seuth 2.1.2.RELEASE
 - Kafka 2.2.7.RELEASE 
 - Flyway 5.2.4
 - Rest Doc 2.0.3.RELEASE
  
## 특징
 - 개념: '목적'을 달성하기 위해 여러 '행위'들로 나눠지며, 이 '행위'들은 추가되거나 삭제, 변경될 수 있다. 
 - 구현: '데이터 수집'을 위해 'AWS Crawling, GCP Crawling' 으로 나눠지며, Azure Crawling, OpenStack Crawling 으로 추가, 변경될 수 있다.
 - Strategy Pattern
   - 클래스 다이어그램
   ![Class Diagram](https://user-images.githubusercontent.com/3222837/61924456-e0a54100-afa2-11e9-9f21-d9635c53cfb6.png)
   
   - 시퀀스 다이어그램
   ![Sequence Diagram](https://user-images.githubusercontent.com/3222837/61916614-20106500-af84-11e9-9535-1c29df3e53f9.png)
   

    
