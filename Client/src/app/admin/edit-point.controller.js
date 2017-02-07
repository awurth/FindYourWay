
export default function AdminEditPointController ($scope, $stateParams, $state, Point) {
  $scope.point = Point.get({ id: $stateParams.id })

  $scope.save = () => {
    Point.save($scope.point, () => {
      $state.go('admin.points')
    })
  }
}

AdminEditPointController.$inject = ['$scope', '$stateParams', '$state', 'Point']
