
export default function User ($resource, API) {
  return $resource(API.url + '/users/:id', { id: '@_id' }, {
    update: { method: 'PUT' },
    login: {
      method: 'POST',
      url: API.url + '/login'
    },
    register: {
      method: 'POST',
      url: API.url + '/register'
    }
  })
}

User.$inject = ['$resource', 'API']
