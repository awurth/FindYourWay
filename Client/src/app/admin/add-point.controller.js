
export default function AdminAddPointController ($scope, $state, Point) {
  $scope.point = {}

  $scope.save = () => {
    Point.save($scope.point, () => {
      $state.go('admin.points')
    })
  }
}

AdminAddPointController.$inject = ['$scope', '$state', 'Point']
