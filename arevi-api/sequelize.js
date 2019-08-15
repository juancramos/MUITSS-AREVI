const Sequelize = require('sequelize');

module.exports = function (app) {
  let connectionString = app.get('mysql');
  if (process.env.PORT){
    const vars = connectionString.split(';');
    const database = vars[0].split('=')[1];
    const source = vars[1].split('=')[1];
    const usr = vars[2].split('=')[1];
    const psw = vars[3].split('=')[1];
    connectionString = `mysql://${usr}:${psw}@${source}/${database}`
  }
  console.log(connectionString);
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
