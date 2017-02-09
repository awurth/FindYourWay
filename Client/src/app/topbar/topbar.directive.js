
export default function TopbarDirective (AuthService) {
  return {
    restrict: 'E',
    template: require('./topbar.directive.html'),
    scope: {
      loggedIn: '='
    },
    link: ($scope, element, attrs) => {
    }
  }
}

TopbarDirective.$inject = ['AuthService']
