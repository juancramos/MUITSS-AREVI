// Initializes the `user-info` service on path `/user-info`
const createService = require('feathers-sequelize');
const createModel = require('../../models/user-info.model');
const hooks = require('./user-info.hooks');

module.exports = function (app) {
  const Model = createModel(app);
  const paginate = app.get('paginate');

  const options = {
    Model,
    paginate
  };

  // Initialize our service with any options it requires
  app.use('/user-info', createService(options));

  // Get our initialized service so that we can register hooks
  const service = app.service('user-info');

  service.hooks(hooks);
};
