
import './assets/scss/app.scss'

import angular from 'angular'
import resource from 'angular-resource'
import router from 'angular-ui-router'
import ngMap from 'ngmap'
import ngMessages from 'angular-messages'

import config from './config'
import JWTService from './app/authentication/jwt.service'
import AuthService from './app/authentication/authentication.service'
import User from './app/user/user'
import Question from './app/question/question'
import Game from './app/game/game'
import LoginController from './app/authentication/login.controller'
import RegisterController from './app/authentication/register.controller'
import AdminController from './app/admin/admin.controller'
import AdminQuestionsController from './app/admin/questions.controller'
import AdminAddQuestionController from './app/admin/questions.add.controller'
import AdminEditQuestionController from './app/admin/questions.edit.controller'
import TopbarDirective from './app/topbar/topbar.directive'
import CompareToDirective from './app/authentication/compareTo.directive'
import HomeController from './app/home/home.controller'
import GameController from './app/game/game.controller'
import GeoService from './app/game/geo.service'

export default angular.module('app', [resource, router, ngMap, ngMessages])
  .constant('API', {
    url: 'http://localhost:8080/findyourway/api/'
  })
  .factory('User', User)
  .service('JWTService', JWTService)
  .service('AuthService', AuthService)
  .config(config)
  .controller('LoginCtrl', LoginController)
  .controller('RegisterCtrl', RegisterController)
  .controller('AdminCtrl', AdminController)
  .controller('AdminQuestionsCtrl', AdminQuestionsController)
  .controller('AdminAddQuestionCtrl', AdminAddQuestionController)
  .controller('AdminEditQuestionCtrl', AdminEditQuestionController)
  .factory('Question', Question)
  .factory('Game', Game)
  .directive('topbar', TopbarDirective)
  .directive('compareTo', CompareToDirective)
  .controller('HomeCtrl', HomeController)
  .controller('GameCtrl', GameController)
  .service('GeoService', GeoService)
  .run(['$transitions', ($transitions) => {
    $transitions.onSuccess({}, (trans) => {
      let AuthService = trans.injector().get('AuthService')
      AuthService.check()
    })
  }])
