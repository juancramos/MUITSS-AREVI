// Initializes the `enum` service on path `/enum`
const createService = require('./enum.class.js');
const hooks = require('./enum.hooks');

module.exports = function (app) {
  
  const paginate = app.get('paginate');

  const options = {
    paginate
  };

  // Initialize our service with any options it requires
  app.use('/enum', createService(options));

  // Get our initialized service so that we can register hooks
  const service = app.service('enum');

  service.hooks(hooks);
};
