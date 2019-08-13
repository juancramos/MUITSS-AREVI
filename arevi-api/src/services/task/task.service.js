// Initializes the `task` service on path `/task`
const createService = require('feathers-sequelize');
const createModel = require('../../models/task.model');
const hooks = require('./task.hooks');

module.exports = function (app) {
  const Model = createModel(app);
  const paginate = app.get('paginate');

  const options = {
    Model,
    paginate
  };

  // Initialize our service with any options it requires
  app.use('/task', createService(options));

  // Get our initialized service so that we can register hooks
  const service = app.service('task');

  service.hooks(hooks);
};
