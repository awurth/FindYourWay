import './assets/scss/app.scss'

import angular from 'angular'
import resource from 'angular-resource'
import router from 'angular-ui-router'
import ngMap from 'ngmap'

import config from './config'
import JWTService from './app/authentication/jwt.service'
import AuthService from './app/authentication/authentication.service'
import User from './app/user/user'
import Point from './app/point/point'
import LoginController from './app/authentication/login.controller'
import RegisterController from './app/authentication/register.controller'
import AdminPointsController from './app/admin/points.controller'
import AdminAddPointController from './app/admin/add-point.controller'
import AdminEditPointController from './app/admin/edit-point.controller'
import AdminDestinationsController from './app/admin/destinations.controller'
import TopbarDirective from './app/topbar/topbar.directive'
import HomeController from './app/home/home.controller'
import GameController from './app/game/game.controller'

export default angular.module('app', [resource, router, ngMap])
  .constant('API', {
    url: 'http://localhost:8080/findyourway/api/'
  })
  .factory('User', User)
  .service('JWTService', JWTService)
  .service('AuthService', AuthService)
  .config(config)
  .controller('LoginCtrl', LoginController)
  .controller('RegisterCtrl', RegisterController)
  .controller('AdminPointsCtrl', AdminPointsController)
  .controller('AdminAddPointCtrl', AdminAddPointController)
  .controller('AdminEditPointCtrl', AdminEditPointController)
  .controller('AdminDestinationsCtrl', AdminDestinationsController)
  .factory('Point', Point)
  .directive('topbar', TopbarDirective)
  .controller('HomeCtrl', HomeController)
  .controller('GameCtrl', GameController)
