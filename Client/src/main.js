import './assets/scss/app.scss'

import angular from 'angular'
import resource from 'angular-resource'
import router from 'angular-ui-router'
import ngMap from 'ngmap'

import config from './config'
import JWTService from './app/authentication/jwt.service'
import AuthService from './app/authentication/authentication.service'
import User from './app/user/user'
import LoginController from './app/authentication/login.controller'
import RegisterController from './app/authentication/register.controller'
import TopbarDirective from './app/topbar/topbar.directive'
import HomeController from './app/home/home.controller'
import GameController from './app/game/game.controller'

export default angular.module('app', [resource, router, ngMap])
  .constant('API', {
    url: 'http://localhost:8080/workshop-2/api/'
  })
  .factory('User', User)
  .service('JWTService', JWTService)
  .service('AuthService', AuthService)
  .config(config)
  .controller('LoginCtrl', LoginController)
  .controller('RegisterCtrl', RegisterController)
  .directive('topbar', TopbarDirective)
  .controller('HomeCtrl', HomeController)
  .controller('GameCtrl', GameController)
