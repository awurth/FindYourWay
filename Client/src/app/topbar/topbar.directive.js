
export default function TopbarDirective (AuthService) {
  return {
    restrict: 'E',
    template: require('./topbar.directive.html'),
    link: ($scope, element, attrs) => {
    }
  }
}

TopbarDirective.$inject = ['AuthService']
