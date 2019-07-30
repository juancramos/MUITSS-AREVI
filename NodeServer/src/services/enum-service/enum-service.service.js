// Initializes the `enum-service` service on path `/enum-service`
const createService = require('./enum-service.class.js');
const hooks = require('./enum-service.hooks');

module.exports = function (app) {
  
  const paginate = app.get('paginate');

  const options = {
    paginate
  };

  // Initialize our service with any options it requires
  app.use('/enum-service', createService(options));

  // Get our initialized service so that we can register hooks
  const service = app.service('enum-service');

  service.hooks(hooks);
};
