
export default function HomeController ($scope, AuthService) {
  $scope.play = () => {
  }
  AuthService.loggedIn(function (user) {
    console.log(user)
  })
}

HomeController.$inject = ['$scope', 'AuthService']
