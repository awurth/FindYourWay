
export default function AdminEditQuestionController ($scope, $state, NgMap, Question) {
  $scope.googleMapsUrl = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBevGWdiDClK7DvnpjA0l96DcaIp_NqD6g'
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

    if (!$scope.errors.name && !$scope.errors.latitude && !$scope.errors.longitude && !$scope.errors.hint) {
      if ($scope.editIndex === null) {
        $scope.points.push($scope.point)
      } else {
        $scope.points[$scope.editIndex] = $scope.point
        $scope.editIndex = null
      }

      $scope.point = {}
    }
  }

  $scope.editPoint = (point) => {
    $scope.point = point
    $scope.editIndex = $scope.points.indexOf(point)
  }

  $scope.deletePoint = (point) => {
    $scope.points.splice($scope.points.indexOf(point), 1)
  }

  $scope.submitQuestion = () => {
    Question.save({ points: $scope.points }, () => {
      $state.go('admin.questions')
    })
  }
}

AdminEditQuestionController.$inject = ['$scope', '$state', 'NgMap', 'Question']
