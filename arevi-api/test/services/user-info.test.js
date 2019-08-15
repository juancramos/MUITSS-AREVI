const assert = require('assert');
const app = require('../.././/app');

describe('\'user-info\' service', () => {
  it('registered the service', () => {
    const service = app.service('user-info');

    assert.ok(service, 'Registered the service');
  });
});
