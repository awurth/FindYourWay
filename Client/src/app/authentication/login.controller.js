
export default function LoginController ($scope, AuthService) {
  $scope.user = {}

  $scope.login = () => {
    AuthService.login($scope.user).then((response) => {
      // Redirect
    }, (response) => {
      console.log(response)
    })
  }
}

LoginController.$inject = ['$scope', 'AuthService']
