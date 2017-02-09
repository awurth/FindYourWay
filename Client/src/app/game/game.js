
export default function Game ($resource, API) {
  return $resource(API.url + 'games/:id', { id: '@id' }, {
    update: { method: 'PUT' }
  })
}

Game.$inject = ['$resource', 'API']
