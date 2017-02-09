
export default function AdminQuestionsController ($scope, Question) {
  $scope.questions = Question.query()

  $scope.delete = (question) => {
    Question.delete(question, () => {
      Question.query((questions) => {
        $scope.questions = questions
      })
    })
  }
}

AdminQuestionsController.$inject = ['$scope', 'Question']
