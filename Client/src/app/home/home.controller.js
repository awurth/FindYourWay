
export default function HomeController ($scope, $state, $window, Game) {
  $scope.play = () => {
    Game.save((game) => {
      $window.localStorage.setItem('game_token', game.token)
      $state.go('game', { id: game.id })
    })
  }
}

HomeController.$inject = ['$scope', '$state', '$window', 'Game']
