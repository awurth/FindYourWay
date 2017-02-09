
export default function AdminController ($state, AuthService) {
  AuthService.check().then(() => {
  }, () => {
    $state.go('home')
  })
}

AdminController.$inject = ['$state', 'AuthService']
