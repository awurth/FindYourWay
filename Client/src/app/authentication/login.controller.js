export default function LoginController ($scope, AuthService, $state) {
  $scope.user = {}

  $scope.login = (isValid) => {
    if (isValid) {
      delete $scope.user.passwordConfirmation
      AuthService.login($scope.user).then((response) => {
        $state.go('home')
      }, (response) => {
        $scope.message = 'Erreur: ' + response.data
      })
    } else {
      $scope.message = 'Des erreurs dans les champs sont survenues'
    }
  }
}

LoginController.$inject = ['$scope', 'AuthService', '$state']
