
export default function AdminPointsController ($scope, Point) {
  $scope.points = Point.query()

  $scope.delete = (point) => {
    Point.delete(point, () => {
      Point.query((points) => {
        $scope.points = points
      })
    })
  }
}

AdminPointsController.$inject = ['$scope', 'Point']
