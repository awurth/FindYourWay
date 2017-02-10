
export default function LeaderBoard ($scope, Score) {
  Score.query(function (data) {
    console.log(data)
    $scope.scores = data
  })
}

LeaderBoard.$inject = ['$scope', 'Score']
