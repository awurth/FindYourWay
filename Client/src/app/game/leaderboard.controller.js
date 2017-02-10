
export default function LeaderBoard ($scope, Score) {
  $scope.offset = 0
  $scope.currentLimit = 5
  $scope.limits = {3: 3, 5: 5, 10: 10, 15: 15, 20: 20, 50: 50}
  Score.pagination({offset: $scope.offset, limit: $scope.currentLimit}, function (data) {
    console.log(data)
    $scope.scores = data
  })
  $scope.limitChanged = (newLimit) => {
    $scope.currentLimit = newLimit
    Score.pagination({offset: $scope.offset, limit: $scope.currentLimit}, function (data) {
      console.log(data)
      $scope.scores = data
    })
  }
}

LeaderBoard.$inject = ['$scope', 'Score']
