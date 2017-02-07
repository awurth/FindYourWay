
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
    /*******************************/
    /* ---------- ADMIN ---------- */
    /*******************************/
    .state('admin', {
      abstract: true,
      url: '/admin',
      template: require('./app/admin/admin.html')
    })
    .state('admin.points', {
      url: '/points',
      template: require('./app/admin/points.html'),
      controller: 'AdminPointsCtrl'
    })
    .state('admin.points.add', {
      url: '/add',
      template: require('./app/admin/point-form.html'),
      controller: 'AdminAddPointCtrl'
    })
    .state('admin.points.edit', {
      url: '/{id}/edit',
      template: require('./app/admin/point-form.html'),
      controller: 'AdminEditPointCtrl'
    })
    .state('admin.destinations', {
      url: '/destinations',
      template: require('./app/admin/destinations.html'),
      controller: 'AdminDestinationsCtrl'
    })
    .state('game', {
      url: '/game',
      template: require('./app/game/game.html'),
      controller: 'GameCtrl'
    })
}
