
export default function GameController ($scope, $state, $stateParams, $window, NgMap, GeoService, Game, Question) {
  $scope.googleMapsUrl = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBevGWdiDClK7DvnpjA0l96DcaIp_NqD6g'
  $scope.flagIconUrl = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png'
  $scope.labels = 'ABCDE'
  $scope.score = 0
  let token = $window.localStorage.getItem('game_token')
  if (!token) {
    $state.go('home')
  }

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

  $scope.hints = []
  $scope.userPoints = []
  $scope.userPoint = {}
  $scope.selecting = false

  NgMap.getMap().then((map) => {
    map.addListener('click', (e) => {
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

        if ($scope.selecting) {
          $scope.userPoints[$scope.userPoints.length - 1] = $scope.userPoint
        } else {
          $scope.userPoints.push($scope.userPoint)
        }

        $scope.selecting = true
      })
    })
  })

  $scope.validatePoint = () => {
    $scope.game.distance = 100
    let distance = GeoService.getKmDistance(
      $scope.userPoint.latitude,
      $scope.userPoint.longitude,
      $scope.questionPoint.latitude,
      $scope.questionPoint.longitude
    )

    if (distance <= $scope.game.distance) {
      $scope.score += 10
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

    $scope.userPoint = {}
    $scope.questionPointIndex++
    $scope.questionPoint = $scope.questionPoints[$scope.questionPointIndex]
    $scope.selecting = false
  }
}

GameController.$inject = ['$scope', '$state', '$stateParams', '$window', 'NgMap', 'GeoService', 'Game', 'Question']
