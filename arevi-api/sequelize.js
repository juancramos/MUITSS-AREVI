const Sequelize = require('sequelize');

module.exports = function (app) {
  const connectionString = app.get('mysql');
  const sequelize = new Sequelize(connectionString, {
    dialect: 'mysql',
    logging: false,
    operatorsAliases: false,
    define: {
      freezeTableName: true
    }
  });
  const oldSetup = app.setup;

  app.set('sequelizeClient', sequelize);

  app.setup = function (...args) {
    const result = oldSetup.apply(this, args);

    // Set up data relationships
    const models = sequelize.models;
    Object.keys(models).forEach(name => {
      if ('associate' in models[name]) {
        models[name].associate(models);
      }
    });

    // Sync to the database    
    sequelize.sync({
      force: true
    });

    return result;
  };
};
