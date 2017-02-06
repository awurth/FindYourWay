
export default function route ($stateProvider) {
  $stateProvider
    .state('home', {
      url: '/',
      template: require('./app/home/home.html'),
      controller: 'HomeCtrl'
    })
    .state('login', {
      url: '/login',
      template: require('./app/authentication/login.html'),
      controller: 'LoginCtrl'
    })
    .state('register', {
      url: '/register',
      template: require('./app/authentication/register.html'),
      controller: 'RegisterCtrl'
    })
    .state('game', {
      url: '/game',
      template: require('./app/game/game.html'),
      controller: 'GameCtrl'
    })
}
