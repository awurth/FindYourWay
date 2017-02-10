
export default function Question ($resource, API) {
  return $resource(API.url + 'questions/:id', { id: '@id' }, {
    update: { method: 'PUT' }
  })
}

Question.$inject = ['$resource', 'API']
