# DBook Web

**Description : 2020 대구소프트웨어 DBook 나르샤 웹 페이지 예시 자료**



![img](https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F60db7c5e-3557-4aa8-a3d0-c015645dccc9%2FUntitled.png?table=block&id=5e022ac5-b462-4900-afc9-c34b89add8af&width=3840&userId=779ce7c6-5c92-476c-bee4-050c75c24982&cache=v2)



## Stack

- Frontend
  - HTML
  - CSS
  - Javascript
  - Jquery
- Backend
  - Java
  - SpringBoot
  - JPA
  - MySQL



## 시작하기

프로젝트를 클론합니다.

```
git clone https://github.com/tmdgjs/DBook-WEB
```

application.yaml 에서 DB 정보를 설정해줍니다.

```java
spring:
      datasource:
            url: jdbc:mysql://{URL}/{DATABASE}
            username : {USERNAME}
            password : {PASSWORD}
```

접속 시 웹 페이지를 확인하실 수 있습니다.

```
https://localhost:8080
```

