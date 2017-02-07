
export default function AdminAddPointController ($scope, $state, NgMap, Question) {
  $scope.googleMapsUrl = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBevGWdiDClK7DvnpjA0l96DcaIp_NqD6g'
  $scope.point = {}

  NgMap.getMap().then((map) => {
    map.addListener('click', (e) => {
      $scope.$apply(() => {
        $scope.point.latitude = e.latLng.lat()
        $scope.point.longitude = e.latLng.lng()
      })
    })
  })
}

AdminAddPointController.$inject = ['$scope', '$state', 'NgMap', 'Question']
