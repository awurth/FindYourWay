
export default function LeaderBoard ($scope, Score) {
  $scope.offset = 0
  $scope.currentLimit = 5
  $scope.limits = {3: 3, 5: 5, 10: 10, 15: 15, 20: 20, 50: 50}
  Score.pagination({offset: 0, limit: $scope.currentLimit}, function (data) {
    $scope.scores = data
  })
  $scope.limitChanged = (newLimit) => {
    $scope.currentLimit = newLimit
    Score.pagination({offset: $scope.offset, limit: $scope.currentLimit}, function (data) {
      $scope.scores = data
    })
  }
  $scope.nextScore = () => {
    Score.pagination({offset: ($scope.offset + $scope.currentLimit), limit: $scope.currentLimit}, function (data) {
      if (data.length > 0) {
        $scope.offset += $scope.currentLimit
        $scope.scores = data
      }
    })
  }
  $scope.beforeScore = () => {
    if (($scope.offset - $scope.currentLimit) >= 0) {
      $scope.offset -= $scope.currentLimit
      Score.pagination({offset: $scope.offset, limit: $scope.currentLimit}, function (data) {
        $scope.scores = data
      })
    } else {
      $scope.offset = 0
      Score.pagination({offset: 0, limit: $scope.currentLimit}, function (data) {
        $scope.scores = data
      })
    }
  }
}

LeaderBoard.$inject = ['$scope', 'Score']
