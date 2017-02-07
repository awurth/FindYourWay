
export default function Point ($resource, API) {
  return $resource(API.url + '/points/:id', { id: '@id' }, {
    update: { method: 'PUT' }
  })
}

Point.$inject = ['$resource', 'API']
