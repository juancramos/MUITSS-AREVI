const preventChangeRole = require('../../hooks/user/role-hook')
const { authenticate } = require('@feathersjs/authentication').hooks;
const checkPermissions = require('feathers-permissions');
const { iff } = require('feathers-hooks-common');
const { restrictToOwner } = require('feathers-authentication-hooks');
const { hashPassword } = require('@feathersjs/authentication-local').hooks;
const restrict = [
  authenticate('jwt'),
  checkPermissions({
    roles: ['admin'],
    field: 'role',
    error: false
  }),
  iff(context => !context.params.permitted,
    restrictToOwner({ idField: 'id', ownerField: 'id' })
  )
];

module.exports = {
  before: {
    all: [],
    find: [...restrict],
    get: [...restrict],
    create: [preventChangeRole(), hashPassword()],
    update: [preventChangeRole(), ...restrict, hashPassword()],
    patch: [preventChangeRole(), ...restrict, hashPassword()],
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
