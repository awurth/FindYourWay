import route from './routes'

export default function config ($httpProvider, $stateProvider, $urlRouterProvider, $locationProvider, $q) {
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
      },
      responseError: (response) => {
        response.responseError = (response) => {
          if (response.status === 401) {
            JWTService.removeToken()
          }
        }
        return response
      }
    }
  }])

  $httpProvider.interceptors.push(['JWTService', (JWTService) => {
    return {

    }
  }])

  // Application routes
  route($stateProvider)
  $urlRouterProvider.otherwise('/')
}

config.$inject = ['$httpProvider', '$stateProvider', '$urlRouterProvider', '$locationProvider']
