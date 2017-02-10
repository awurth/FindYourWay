
export default function Score ($resource, API) {
  return $resource(API.url + 'scores/:id', { id: '@id' }, {
    update: { method: 'PUT' }
  })
}

Score.$inject = ['$resource', 'API']
