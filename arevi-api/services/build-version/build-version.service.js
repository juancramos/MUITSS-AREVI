// Initializes the `build-version` service on path `/build-version`
const createService = require('feathers-sequelize');
const createModel = require('../../models/build-version.model');
const hooks = require('./build-version.hooks');

module.exports = function (app) {
  const Model = createModel(app);
  const paginate = app.get('paginate');

  const options = {
    Model,
    paginate
  };

  // Initialize our service with any options it requires
  app.use('/build-version', createService(options));

  // Get our initialized service so that we can register hooks
  const service = app.service('build-version');

  service.hooks(hooks);
};
