
export default function GameController ($scope, $state, $stateParams, $window, NgMap, GeoService, Game, Score, Question) {
  $scope.googleMapsUrl = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBevGWdiDClK7DvnpjA0l96DcaIp_NqD6g'
  $scope.flagIconUrl = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png'
  $scope.labels = 'ABCDE'
  $scope.score = 0

  // Redirect to homepage if game token is not defined
  let token = $window.localStorage.getItem('game_token')
  if (!token) {
    $state.go('home')
  }

  // Get current game from id
  Game.save({ id: $stateParams.id }, token, (game) => {
    $scope.game = game

    Question.get({ id: game.question.id }, (question) => {
      $scope.game.question = question

      $scope.questionPoints = []

      let finalPoint = null
      $scope.game.question.points.forEach((point) => {
        if (point.final) {
          finalPoint = point
        } else {
          $scope.questionPoints.push(point)
        }
      })

      $scope.questionPoints.push(finalPoint)

      $scope.questionPointIndex = 0
      $scope.questionPoint = $scope.questionPoints[$scope.questionPointIndex]
    })
  })

  // Unlocked hints for final point
  $scope.hints = []
  // Points that the user clicked on the map
  $scope.userPoints = []
  // Current point clicked by the user
  $scope.userPoint = {}
  // True if the point has not been validated yet (to be able to replace it without adding a new point to the list)
  $scope.selecting = false

  NgMap.getMap().then((map) => {
    map.addListener('click', (e) => {
      // If we already placed all points, do nothing when we click on the map
      if ($scope.questionPointIndex >= $scope.questionPoints.length) {
        return
      }

      $scope.$apply(() => {
        $scope.userPoint = {
          latitude: e.latLng.lat(),
          longitude: e.latLng.lng(),
          name: $scope.questionPoint.name,
          final: $scope.questionPoint.final
        }

        // If the point has not been validated
        if ($scope.selecting) {
          // Update its position
          $scope.userPoints[$scope.userPoints.length - 1] = $scope.userPoint
        } else {
          // Else add it to the list
          $scope.userPoints.push($scope.userPoint)
        }

        $scope.selecting = true
      })
    })
  })

  /**
   * Validate point position update score
   */
  $scope.validatePoint = () => {
    $scope.game.distance = 100
    let distance = GeoService.getKmDistance(
      $scope.userPoint.latitude,
      $scope.userPoint.longitude,
      $scope.questionPoint.latitude,
      $scope.questionPoint.longitude
    )

    // Score calculation
    if (distance <= $scope.game.distance) {
      $scope.score += 10
      // Add hint to unlocked hints list
      $scope.hints.push($scope.game.question.hints[$scope.questionPointIndex])
    } else if (distance <= ($scope.game.distance * 2)) {
      $scope.score += 8
    } else if (distance <= ($scope.game.distance * 3)) {
      $scope.score += 6
    } else if (distance <= ($scope.game.distance * 5)) {
      $scope.score += 3
    } else if (distance <= ($scope.game.distance * 10)) {
      $scope.score += 1
    }

    // Reset current location and move to next question
    $scope.userPoint = {}
    $scope.questionPointIndex++
    $scope.questionPoint = $scope.questionPoints[$scope.questionPointIndex]
    $scope.selecting = false

    // If the game is finished
    if ($scope.questionPointIndex >= $scope.questionPoints.length) {
      let score = {
        question: {
          id: $scope.game.question.id
        },
        value: $scope.score
      }

      Score.save(score, () => {
        $state.go('leaderboard')
      })
    }
  }
}

GameController.$inject = ['$scope', '$state', '$stateParams', '$window', 'NgMap', 'GeoService', 'Game', 'Score', 'Question']
