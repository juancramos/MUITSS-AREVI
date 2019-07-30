const user = require('./user/user.service.js');
const profile = require('./profile/profile.service.js');
const enum = require('./enum/enum.service.js');
// eslint-disable-next-line no-unused-vars
module.exports = function (app) {
  app.configure(user);
  app.configure(profile);
  app.configure(enum);
};
