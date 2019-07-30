// See http://docs.sequelizejs.com/en/latest/docs/models-definition/
// for more of what you can do here.
const Sequelize = require('sequelize');
const DataTypes = Sequelize.DataTypes;

module.exports = function (app) {
  const sequelizeClient = app.get('sequelizeClient');
  const profile = sequelizeClient.define('profile', {
    id: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    fullName: {
      type: DataTypes.STRING,
      allowNull: false
    },
    gender: {
      type: DataTypes.STRING,
      allowNull: false,
      enum: ['Female', 'Male', 'Other (specify)']
    },
    genderOther: {
      type: DataTypes.STRING,
      allowNull: false
    },
    age: {
      type: DataTypes.STRING,
      allowNull: false,
      enum: ['17 or younger', '18-20', '21-29', '30-39', '40-49', '50-59', '60 or older']
    },
    visualIlness: {
      type: DataTypes.STRING,
      allowNull: false
    },
    occupation: {
      type: DataTypes.STRING,
      allowNull: false
    },
    education: {
      type: DataTypes.STRING,
      allowNull: false,
      enum: ['Less than high school degree', 'High school degree or equivalent', 'Some college but no degree', 'Associate degree', 'Bachelor degree', 'Graduate degree']
    }
  }, {
      hooks: {
        beforeCount(options) {
          options.raw = true;
        }
      }
    });

  // eslint-disable-next-line no-unused-vars
  profile.associate = function (models) {
    // Define associations here
    // See http://docs.sequelizejs.com/en/latest/docs/associations/
  };

  return profile;
};
