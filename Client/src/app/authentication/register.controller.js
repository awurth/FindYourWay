
export default function RegisterController ($scope, AuthService, $state) {
  AuthService.check().then(() => {
    $state.go('home')
  })

  $scope.user = {}
  $scope.register = (isValid) => {
    if (isValid) {
      delete $scope.user.passwordConfirmation
      AuthService.register($scope.user).then((response) => {
        $state.go('login')
      }, (response) => {
        $scope.message = 'Erreur: ' + response.data
      })
    } else {
      $scope.message = 'Des erreurs dans les champs sont survenues'
    }
  }
}

RegisterController.$inject = ['$scope', 'AuthService', '$state']
