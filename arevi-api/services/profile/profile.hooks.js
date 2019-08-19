const setEnabled = require('../../hooks/profile/setEnabled-hook');
const jsonParse = require('../../hooks/profile/jsonParse-hook');
const { authenticate } = require('@feathersjs/authentication').hooks;
const checkPermissions = require('feathers-permissions');
const { iff } = require('feathers-hooks-common');
const { restrictToOwner } = require('feathers-authentication-hooks');
const restrict = [
  checkPermissions({
    roles: ['admin'],
    field: 'role',
    error: false
  }),
  iff(context => !context.params.permitted,
    restrictToOwner({ idField: 'id', ownerField: 'userId' })
  )
];

module.exports = {
  before: {
    all: [authenticate('jwt')],
    find: [...restrict],
    get: [...restrict],
    create: [setEnabled()],
    update: [...restrict],
    patch: [...restrict],
    remove: [...restrict]
  },

  after: {
    all: [],
    find: [jsonParse()],
    get: [jsonParse()],
    create: [],
    update: [],
    patch: [],
    remove: []
  },

  error: {
    all: [],
    find: [],
    get: [],
    create: [],
    update: [],
    patch: [],
    remove: []
  }
};
