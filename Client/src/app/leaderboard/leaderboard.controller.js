
export default function LeaderBoard ($scope, Score) {
  Score.query(function (data) {
    $scope.scores = data
  })
}

LeaderBoard.$inject = ['$scope', 'Score']
