const { authenticate } = require('@feathersjs/authentication').hooks;
const checkPermissions = require('feathers-permissions');
const { iff } = require('feathers-hooks-common');
const { restrictToOwner } = require('feathers-authentication-hooks');
const restrict = [
  authenticate('jwt'),
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
    all: [],
    find: [...restrict],
    get: [...restrict],
    create: [],
    update: [...restrict],
    patch: [...restrict],
    remove: [...restrict]
  },

  after: {
    all: [],
    find: [],
    get: [],
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
