// Initializes the `assessment` service on path `/assessment`
const createService = require('feathers-sequelize');
const createModel = require('../../models/assessment.model');
const hooks = require('./assessment.hooks');

module.exports = function (app) {
  const Model = createModel(app);
  const paginate = app.get('paginate');

  const options = {
    Model,
    paginate
  };

  // Initialize our service with any options it requires
  app.use('/assessment', createService(options));

  // Get our initialized service so that we can register hooks
  const service = app.service('assessment');

  service.hooks(hooks);
};
