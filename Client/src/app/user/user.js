
export default function User ($resource, API) {
  return $resource(API.url + 'users/:id', { id: '@id' }, {
    update: { method: 'PUT' },
    login: {
      method: 'POST',
      url: API.url + '/authentication'
    },
    register: {
      method: 'POST',
      url: API.url + '/user/signup'
    }
  })
}

User.$inject = ['$resource', 'API']
