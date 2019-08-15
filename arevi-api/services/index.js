const EnumService = require('./enum-service/enum-service.service.js');const user = require('./user/user.service.js');
const userInfo = require('./user-info/user-info.service.js');
const task = require('./task/task.service.js');
const profile = require('./profile/profile.service.js');
const round = require('./round/round.service.js');
// eslint-disable-next-line no-unused-vars
module.exports = function (app) {
  app.configure(EnumService);
  app.configure(user);
  app.configure(userInfo);
  app.configure(task);
  app.configure(profile);
  app.configure(round);
};
