
export default class AuthService {
  constructor ($rootScope, JWTService, User, $state) {
    this.$rootScope = $rootScope
    this.JWTService = JWTService
    this.User = User
    this.$state = $state
  }

  check () {
    this.$rootScope.authenticated = this.JWTService.getToken() != null
    return this.User.loggedIn((user) => {
      this.$rootScope.user = user
      this.$rootScope.authenticated = true
    }, (response) => {
      this.$state.go('login')
    }).$promise
  }

  login (credentials) {
    return this.User.login(credentials, (user) => {
      this.JWTService.setToken(user.token)
    }).$promise
  }

  register (credentials) {
    return this.User.register(credentials).$promise
  }

  logout () {
    // User.signout()
    this.JWTService.removeToken()
  }
}

AuthService.$inject = ['$rootScope', 'JWTService', 'User', '$state']
