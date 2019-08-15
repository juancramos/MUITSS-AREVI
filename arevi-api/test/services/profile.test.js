const assert = require('assert');
const app = require('../.././/app');

describe('\'profile\' service', () => {
  it('registered the service', () => {
    const service = app.service('profile');

    assert.ok(service, 'Registered the service');
  });
});
