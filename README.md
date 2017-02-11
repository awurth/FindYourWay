# FindYourWay
> A mini-game made in 5 days using Google Maps images that drops the player in a random location and challenges them to work out where they are.

<p align="center"><img style="margin-bottom:3em;" width="550px"src="http://geoawesomeness.com/wp-content/uploads/2015/08/GeoGuessr-image-Geoawesomeness.jpg">
<br> <br>
[![Packagist](https://img.shields.io/packagist/l/doctrine/orm.svg?style=flat-square)]()  
</p>  <br>


# How to play
1. Go to "Register" page and create an account. You will be redirected to Login page
2. Log in to your account
3. Go to "My account" page and click on the "Manage questions" button
4. Click on the "Add question" button.
5. Click on the google map to add a location and fill the form below to add a name and a hint to your location
6. Click on the "Validate point" button to validate the location and create another one. Repeat steps 5 and 6 five times.
7. Add 5 hints for the final point
8. When you're done adding locations, a green button will show up. Click on it to save the question.
9. Go to homepage and click on the "Play" button. This will select a random question from the database and create a game.
10. You will see a google map, the first location's name and hint. Click on the map to place a marker on it.
11. Click on "Validate location". Your score will show up on the right of the page. Repeat step 10 five times.

> How to be an admin ?

First of all we thought to add a route in order to create an admin user but it is not secured at all. <br/>
So we have decided that the only way to add it is in the database. You just have to create a normal user but with ```UserRole.ADMIN``` as Role

<br/>

# Server
RESTful API written in Java, using Jax-RS and JPA it also follows the Entity-Control-Boundary pattern.

Documentation available at the index page (with docker: http://localhost:8080/findyourway)

## Features
- Account
- CORS Filter
- Digest access authentication
- Documentation (Powered by Swagger.io)
- HATEOAS
- JWT
- Password Hashing
- Role Accounts

## Docker
> A Dockerfile is included, it contains Debian, Wildfly and we will add Postgres in the future. <br/>
The admin login and password for Wildfly is username : ```admin``` , password : ```admin````

- API available at :  http://localhost:8080/findyourway/api/
- Wildfly's dashboard :  http://localhost:9990/

## Installation
### 1. Clone repository
```bash
$ git clone https://github.com/TPCISIIE/FindYourWay.git
```

### 2. Build your Dockerfile
```bash
$ cd FindYourWay/Server/docker
$ docker build --build-arg DB_HOST=127.0.0.1 --build-arg DB_NAME=findyourway --build-arg DB_USER=root --build-arg DB_PASS=root --tag api-server .
```

### 4. Run an instance of a Postgres Server
> Example given : 

```bash
$ docker run  -p 5432:5432 -itd --restart always \
--env 'DB_USER=root' --env 'DB_PASS=root' \
--env 'DB_NAME=findyourway' \
sameersbn/postgresql:9.6-2
```
### 3. Run your fresh container
```bash
$ docker run -p 8080:8080 -p 9990:9990 api-server
```

<hr>

# Client

## Engines used
- AngularJS
- Electron
- Webpack
- Bootstrap SASS

### Why Webpack ?
- It compiles ES6 into ES5 (by using Babel)
- It isolates the code by using modules
- It includes a hot reload

## Installation
### 1. Clone repository if it is done yet
```bash
$ git clone https://github.com/TPCISIIE/FindYourWay.git
```

### 2. Install dependencies
```bash
$ cd FindYourWay/Client
$ npm install
$ bower install
```

### 3. Run the application

Run the dev server (it compiles the project, run a watcher and open your browser at http://localhost:4000)
``` bash
$ npm run dev
```

Build the project (http://localhost:8080)
``` bash
$ npm run build
```

Run the project with Electron (do not forget to build the project before)
``` bash
$ npm run electron
```

## Authors
Xavier CHOPIN, Corentin LABROCHE, David LEBRUN and Alexis WURTH

## License
This application is open-sourced software licensed under the MIT license.
