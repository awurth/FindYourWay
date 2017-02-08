
export default class JWTService {
  constructor ($window) {
    this.$window = $window
  }

  setToken (token) {
    this.$window.localStorage.setItem('token_date', Date.now())
    this.$window.localStorage.setItem('token', token)
  }

  getToken () {
    let date = this.$window.localStorage.getItem('token_date')

    // On teste la validité du token
    // Si la dernière requête était il y a plus de 30 min (1800000 ms)
    // on supprime le token, sinon on actualise la date
    if (date && Date.now() - parseInt(date) > 1800000) {
      this.$window.localStorage.removeItem('token')
    }

    return this.$window.localStorage.getItem('token')
  }

  removeToken () {
    this.$window.localStorage.removeItem('token')
  }
}

JWTService.$inject = ['$window']
