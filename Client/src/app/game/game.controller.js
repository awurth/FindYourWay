
export default function GameController ($scope, $state, $stateParams, $window, NgMap, GeoService, Game, Question) {
  $scope.googleMapsUrl = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBevGWdiDClK7DvnpjA0l96DcaIp_NqD6g'
  $scope.flagIconUrl = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png'
  $scope.labels = 'ABCDE'
  let token = $window.localStorage.getItem('game_token')
  if (!token) {
    $state.go('home')
  }

  Game.save({ id: $stateParams.id }, token, (game) => {
    $scope.game = game
    /* $scope.game = {
      id: '68848644s8v68rzbr4h44hred4r4eqhq646qr68hqe',
      distance: 50,
      question: {
        id: '4d4end46drset4t4q4tj4646s46j4f4nt6ds4',
        hints: [
          'Indice 1',
          'Indice 2',
          'Indice 3',
          'Indice 4',
          'Indice 5'
        ],
        points: [
          {
            id: 'd46h6hr46n4d6e',
            name: 'Nancy',
            latitude: 48.690,
            longitude: 6.181,
            hint: 'Indice du point n°1'
          }, {
            id: 'dfn4fd6n6d5rn6fs6n',
            name: 'Paris',
            latitude: 48.856,
            longitude: 2.345,
            hint: 'Indice du point n°2'
          }, {
            id: 'dfsb4rns4n6s486rn34',
            name: 'Rennes',
            latitude: 48.114,
            longitude: -1.677,
            hint: 'Indice du point n°3'
          }, {
            id: '4wd6f4n4fdw4n6r4',
            name: 'Toulouse',
            latitude: 43.602,
            longitude: 1.441,
            hint: 'Indice du point n°4'
          }, {
            id: 'wdf5nwdsrn6d65r4n8694',
            name: 'Montpellier',
            latitude: 43.607,
            longitude: 3.876,
            hint: 'Indice du point n°5'
          }, {
            id: 'dwbs46b5rs4ds5rn6d4',
            name: 'Marseille',
            latitude: 43.293,
            longitude: 5.373,
            hint: 'Indice du point n°6',
            final: true
          }
        ]
      }
    } */
    console.log(game)
    Question.get({ id: game.question.id }, (question) => {
      console.log('OK:')
      console.log(question)
    }, (response) => {
      console.log('Error:')
      console.log(response)
    })

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
    if (GeoService.getKmDistance($scope.userPoint.latitude, $scope.userPoint.longitude, $scope.questionPoint.latitude, $scope.questionPoint.longitude) <= $scope.game.distance) {
      $scope.hints.push($scope.game.question.hints[$scope.questionPointIndex])
    }

    $scope.userPoint = {}
    $scope.questionPointIndex++
    $scope.questionPoint = $scope.questionPoints[$scope.questionPointIndex]
    $scope.selecting = false
  }
}

GameController.$inject = ['$scope', '$state', '$stateParams', '$window', 'NgMap', 'GeoService', 'Game', 'Question']
