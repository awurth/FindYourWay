
export default function Score ($resource, API) {
  return $resource(API.url + 'scores/:id', { id: '@id' }, {
    update: { method: 'PUT' },
    pagination: {
      method: 'GET',
      url: API.url + 'scores/pages',
      params: {offset: '@offset', limit: '@limit'},
      isArray: true
    }
  })
}

Score.$inject = ['$resource', 'API']
