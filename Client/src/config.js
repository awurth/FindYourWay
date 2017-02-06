import route from './routes'

export default function config ($httpProvider, $stateProvider, $urlRouterProvider, $locationProvider) {
  $locationProvider.html5Mode(true)

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

config.$inject = ['$httpProvider', '$stateProvider', '$urlRouterProvider', '$locationProvider']
