
export default function RegisterController ($scope, AuthService) {
  $scope.user = {}

  $scope.register = () => {
    AuthService.register($scope.user).then((response) => {
      // Redirect
    }, (response) => {
      console.log(response)
    })
  }
}

RegisterController.$inject = ['$scope', 'AuthService']
