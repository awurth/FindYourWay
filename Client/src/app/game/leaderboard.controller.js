
export default function LeaderBoard ($scope, Score) {
  $scope.offset = 0
  $scope.limit = 3
  Score.pagination({offset: $scope.offset, limit: $scope.limit}, function (data) {
    console.log(data)
    $scope.scores = data
  })
}

LeaderBoard.$inject = ['$scope', 'Score']
