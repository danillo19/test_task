# test_task
Simple React + Spring web app.
# Table of contents
- [Introduction](https://github.com/danillo19/test_task/blob/master/README.md#introduction)
- [Requirements](https://github.com/danillo19/test_task/blob/master/README.md#requirements)
- [Quick start](https://github.com/danillo19/test_task/blob/master/README.md#quick-start)
- [How to work with it](https://github.com/danillo19/test_task/blob/master/README.md#how-to-work-with-it)
- [Screenshots](https://github.com/danillo19/test_task/blob/master/README.md#screenshots)

## Introduction
Simple React + Spring Boot + MySQL (inside docker container) web application, where you can create/update/delete/get banners/categories and search by their names.

## Requirements
App can be run locally, requirements a listed below.

### Local
- [Spring Boot](https://spring.io/projects/spring-boot)
- [React](https://reactjs.org/)
- [Docker](https://www.docker.com/products/docker-desktop/)

## Quick start
### Configure database

Build docker image
```
$ cd backend
$ docker build -t test_task .
```
Start container with environment arguments.
```
$  docker run -d --name <container_id> -p 3306:3306 -e MYSQL_ROOT_PASSWORD=<password> -e MYSQL_DATABASE=<db_name> test:latest
```
By default app works with `MYSQL_ROOT_PASSWORD=secret` and `MYSQL_DATABASE=mySchema`. If you change it, don't forget to change them in `application.properties` too.
MySql database started on port `3306` (you can specify external port :)).
```
 $ docker run -d --name test_task -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=mySchema test_task:latest
```

### Start the backend
(inside backend directory)
```
$ mvn clean package
$ jar -jar target/backend-0.0.1-SNAPSHOT.jar
```
Spring boot aplication has been started on port `8080` (you can specify it in `application.properties`)

### Start the frontend
```
$ cd ..
$ cd frontend
$ npm install
$ npm run start
```
Frontend has been started on port `3000` .
You can see it in browser on `localhost:3000/sign_in`

## How to work with it
By default only /bid endpoint is public.
In database was preloaded default user(admin) with username: `admin` and password: `admin` to pass autentication and access private 
`/banner/*` and `/category/*` endpoints.


# Screenshots
![image](https://user-images.githubusercontent.com/71901824/178946087-f1c95dc6-0b5e-46eb-8b76-f34ca70c4835.png)
![image](https://user-images.githubusercontent.com/71901824/178946130-b9319b5e-8131-471f-a45b-60d8c4c0deeb.png)
![image](https://user-images.githubusercontent.com/71901824/178946154-68c14dc9-e164-4562-b402-b21d38ad9186.png)
![image](https://user-images.githubusercontent.com/71901824/178946186-98ce443f-e263-49a5-8c1c-0de30ee8bc1e.png)




