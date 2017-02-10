
export default function AdminQuestionsController ($scope, Question) {
  $scope.questions = Question.query()

  $scope.getPointsNames = (points) => {
    let names = ''
    points.forEach((point, index) => {
      names += index < points.length - 1 ? point.name + ', ' : point.name
    })

    return names
  }

  $scope.delete = (question) => {
    Question.delete(question, () => {
      Question.query((questions) => {
        $scope.questions = questions
      })
    })
  }
}

AdminQuestionsController.$inject = ['$scope', 'Question']
