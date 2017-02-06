import route from './routes'

export default function config ($httpProvider, $stateProvider, $urlRouterProvider, $locationProvider, $qProvider) {
  $locationProvider.html5Mode({
    'enabled': true,
    'requireBase': false
  })
  $qProvider.errorOnUnhandledRejections(false)

  // Add Authorization header with JWT if user is authenticated
  $httpProvider.interceptors.push(['JWTService', (JWTService) => {
    return {
      request: (config) => {
        let token = JWTService.getToken()
        if (token != null) {
          config.headers.authorization = 'Bearer ' + token
        }

        return config
      }
    }
  }])

  // Application routes
  route($stateProvider)
  $urlRouterProvider.otherwise('/')
}

config.$inject = ['$httpProvider', '$stateProvider', '$urlRouterProvider', '$locationProvider', '$qProvider']
