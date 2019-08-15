const assert = require('assert');
const app = require('../.././/app');

describe('\'round\' service', () => {
  it('registered the service', () => {
    const service = app.service('round');

    assert.ok(service, 'Registered the service');
  });
});
