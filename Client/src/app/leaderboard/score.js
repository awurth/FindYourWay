
export default function Score ($resource, API) {
  return $resource(API.url + 'scores/:id', { id: '@id' }, {
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

Score.$inject = ['$resource', 'API']
