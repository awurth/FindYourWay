
export default function AdminAddQuestionController ($scope, $state, NgMap, Question) {
  $scope.googleMapsUrl = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBevGWdiDClK7DvnpjA0l96DcaIp_NqD6g'
  $scope.flagIconUrl = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png'
  $scope.pointsCount = 6
  $scope.points = []
  $scope.point = {}
  $scope.editIndex = null

  NgMap.getMap().then((map) => {
    map.addListener('click', (e) => {
      $scope.$apply(() => {
        $scope.point.latitude = e.latLng.lat()
        $scope.point.longitude = e.latLng.lng()
      })
    })
  })

  $scope.validatePoint = () => {
    $scope.errors = {
      name: $scope.point.name ? '' : 'The point must have a name',
      latitude: $scope.point.latitude ? '' : 'The point must have a latitude',
      longitude: $scope.point.longitude ? '' : 'The point must have a longitude',
      hint: $scope.point.hint ? '' : 'The point must have a hint'
    }

    // If points are valid
    if (!$scope.errors.name && !$scope.errors.latitude && !$scope.errors.longitude && !$scope.errors.hint) {
      // If we are creating a new point
      if ($scope.editIndex === null) {
        // Set final point
        if ($scope.points.length === $scope.pointsCount - 1) {
          // Set last point final only if there is no final point yet
          let final = true
          $scope.points.forEach((e) => {
            if (e.final) {
              final = false
            }
          })
          $scope.point.final = final
        }

        $scope.points.push($scope.point)
      } else {
        // If we are editing an existing point
        $scope.points[$scope.editIndex] = $scope.point
        $scope.editIndex = null
      }

      $scope.point = {}
    }
  }

  // Set given point as final
  $scope.setFinalPoint = (point) => {
    $scope.points.forEach((e) => {
      e.final = false
    })

    $scope.points[$scope.points.indexOf(point)].final = true
  }

  // Edit given point
  $scope.editPoint = (point) => {
    $scope.point = point
    $scope.editIndex = $scope.points.indexOf(point)
  }

  // Delete point from the list
  $scope.deletePoint = (point) => {
    $scope.points.splice($scope.points.indexOf(point), 1)
  }

  // Save question
  $scope.submitQuestion = () => {
    Question.save({ points: $scope.points }, () => {
      $state.go('admin.questions.all')
    })
  }
}

AdminAddQuestionController.$inject = ['$scope', '$state', 'NgMap', 'Question']
