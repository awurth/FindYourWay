
export default class AuthService {
  constructor ($rootScope, JWTService, User) {
    this.$rootScope = $rootScope
    this.JWTService = JWTService
    this.User = User
  }

  check () {
    this.$rootScope.authenticated = this.JWTService.getToken() != null
    return this.$rootScope.authenticated
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

AuthService.$inject = ['$rootScope', 'JWTService', 'User']
