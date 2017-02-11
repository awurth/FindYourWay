# FindYourWay
> A mini-game made in 5 days using Google Maps images that drops the player in a random location and challenges them to work out where they are.


# How to play
> Alexis or someone please it's 4 am


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
> A Dockerfile is included, it contains Debian, Wildfly and we will add Postgres in the future.
The admin login and password for Wildfly is username : ```admin``` , password : ```admin```
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
$ docker build --build-arg DB_HOST=localhost --build-arg DB_NAME=findyourway --build-arg DB_USER=root --build-arg DB_PASS=root --tag api-server .
```

### 3. Run your fresh container 
```bash
$ docker run -p 8080:8080 -p 9990:9990 -p 5432:5432 api-server
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
