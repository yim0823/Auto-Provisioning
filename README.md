# DCOS - AC(Auto Provisioning)
Java web application 으로 Cloud 제품별 Asset 정보와 서비스 계정을 주기적으로 Crawling 서비스이다.

## 주기능
 - IC는 주기적으로, 
   - OpsNow Database(MySQL) 에서 Asset 정보를
   - Portal Database(MySQL) 에서 Service Account 정보를
   
   DCOS Database(MariaDB와 Cassandra) 로 ETL 한다.
   
 - 데이터 성격에 따라,
    - basic data 는 MariaDB 에
    - extra data 는 Cassandra 에
    
    분리되어 관리된다.  
 
## 환경
 - Java 11
 - Spring-boot-gradle.2.1.5.RELEASE
 - JPA 2.1.5.RELEASE
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
   

    
