
export default function AdminEditPointController ($scope, $stateParams, $state, NgMap, Point) {
  $scope.googleMapsUrl = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBevGWdiDClK7DvnpjA0l96DcaIp_NqD6g'
  $scope.point = Point.get({ id: $stateParams.id })

  NgMap.getMap().then((map) => {
    map.addListener('click', (e) => {
      $scope.$apply(() => {
        $scope.point.latitude = e.latLng.lat()
        $scope.point.longitude = e.latLng.lng()
      })
    })
  })

  $scope.save = () => {
    Point.save($scope.point, () => {
      $state.go('admin.points')
    })
  }
}

AdminEditPointController.$inject = ['$scope', '$stateParams', '$state', 'NgMap', 'Point']
