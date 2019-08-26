(日本語は英語の下にある)

# Spring boot example application: notice board
[https://github.com/saladlam/spring-noticeboard](https://github.com/saladlam/spring-noticeboard)

## Objective
I write this application is for explain the mechanism of different component of Spring framework.

## Function
- Display notice on stated time
- Approval before display
- Multi language UI

## Main component used
- Spring Boot 2.1.X
- Spring MVC 5.1.X with thymeleaf 3.0.X
- Spring Security 5.1.X
- Spring JDBC 5.1.X
- Dozer 5.5.X

## Database
Data stored in embedded H2 database. Schema and data is imported when application start and all data will be lost after application shutdown.

## Prerequisite
- Java SE Development Kit 8 or above
- Internet connection

## Compile and run
Apache Maven wrapper is included, no addition package manager is necessary.

### Compile
```
./mvnw package
```

### Run
```
./mvnw spring-boot:run
```

## Account
| Username | Password | Authorities |
|:-------- |:-------- |:----------- |
| user1    | user1    | USER        |
| user2    | user2    | USER        |
| admin    | admin    | USER, ADMIN |


# Spring bootサンプルアプリケーション: 掲示板
[https://github.com/saladlam/spring-noticeboard](https://github.com/saladlam/spring-noticeboard)

## 目的
このアプリケーションを作るのは、Spring frameworkを構成する、いろいろなコンポーネントの流れを説明するため。

## 機能
- 決められた時メッセージを表示すること
- メッセージを表示する前、許可すること
- 多言語ユーザーインターフェイス

## 使われる主要なコンポーネント
- Spring Boot 2.1.X
- Spring MVC 5.1.X と thymeleaf 3.0.X
- Spring Security 5.1.X
- Spring JDBC 5.1.X
- Dozer 5.5.X

## データベース
データは埋め込み H2 データベースに記憶する。スキーマとデータはアプリケーション起動時がインポートされ、それからアプリケーション終了時全てのデータがなくなった。

## 必要なもの
- Java SE Development Kit 8以降
- インタネット接続

## コンパイルと実行
Apache Mavenラッパーがついていて、だからパッケージマネージャが用意されなくでもいい。

### コンパイルする
```
./mvnw package
```

### 実行する
```
./mvnw spring-boot:run
```

## アカウント
| ユーザーネーム | パスワード | 権限 |
|:-------- |:-------- |:----------- |
| user1    | user1    | USER        |
| user2    | user2    | USER        |
| admin    | admin    | USER, ADMIN |
