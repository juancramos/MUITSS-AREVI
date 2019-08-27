const assert = require('assert');
const app = require('../.././/app');

describe('\'assessment\' service', () => {
  it('registered the service', () => {
    const service = app.service('assessment');

    assert.ok(service, 'Registered the service');
  });
});
