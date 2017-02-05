
export default class AuthService {
  constructor (JWTService, User) {
    this.JWTService = JWTService
    this.User = User
  }

  check () {
    return this.JWTService.getToken() != null
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

AuthService.$inject = ['JWTService', 'User']
