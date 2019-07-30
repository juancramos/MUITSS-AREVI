// See http://docs.sequelizejs.com/en/latest/docs/models-definition/
// for more of what you can do here.
const Sequelize = require('sequelize');
const DataTypes = Sequelize.DataTypes;

module.exports = function (app) {
  const sequelizeClient = app.get('sequelizeClient');
  const profile = sequelizeClient.define('profile',
    {
      id: {
        type: DataTypes.UUID,
        defaultValue: DataTypes.UUIDV4,
        primaryKey: true
      },
      profileNmae: {
        type: DataTypes.STRING,
        allowNull: false
      },
      configuration: {
        type: Sequelize.TEXT,
        get: function () {
          return JSON.parse(this.getDataValue('value'));
        },
        set: function (value) {
          this.setDataValue('value', JSON.stringify(value));
        },
        allowNull: false
      },
      enabled: {
        type: DataTypes.BOOLEAN,
        allowNull: false,
        defaultValue: true
      }
    },
    {
      hooks: {
        beforeCount(options) {
          options.raw = true;
        }
      }
    }
  );

  // eslint-disable-next-line no-unused-vars
  profile.associate = function (models) {
    // Define associations here
    // See http://docs.sequelizejs.com/en/latest/docs/associations/
    profile.hasMany(models.round, { foreignKey: { allowNull: false } });
  };

  return profile;
};
