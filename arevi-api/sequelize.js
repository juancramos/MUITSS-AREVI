const Sequelize = require('sequelize');

module.exports = function (app) {
  const connectionString = app.get('mysql');
  let sequelize = null
  let sequelizeParams = {
    dialect: 'mysql',
    logging: false,
    define: {
      freezeTableName: true
    }
  };
  if (process.env.PORT) {
    const vars = connectionString.split(';');
    const database = vars[0].split('=')[1];
    const source = vars[1].split('=')[1];
    const hostName = source.split(':');
    const usr = vars[2].split('=')[1];
    const psw = vars[3].split('=')[1];

    Object.assign(sequelizeParams, {
      host: hostName[0],
      port: hostName[1]
    });

    sequelize = new Sequelize(database, usr, psw, sequelizeParams);
  }
  else {
    sequelize = new Sequelize(connectionString, sequelizeParams);
  }

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
      force: false
    });

    return result;
  };
};
