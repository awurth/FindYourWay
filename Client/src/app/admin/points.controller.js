
export default function AdminPointsController ($scope, Point) {
  $scope.points = [
    {
      id: 1,
      name: 'Point #1',
      latitude: '164564',
      longitude: '5484646'
    }, {
      id: 2,
      name: 'Point #2',
      latitude: '44537',
      longitude: '453453'
    }, {
      id: 3,
      name: 'Point #3',
      latitude: '7687',
      longitude: '4578'
    }, {
      id: 4,
      name: 'Point #4',
      latitude: '786',
      longitude: '4537'
    }
  ]

  $scope.delete = (point) => {
    Point.delete(point, () => {
      Point.query((points) => {
        $scope.points = points
      })
    })
  }
}

AdminPointsController.$inject = ['$scope', 'Point']
