const assert = require('assert');
const app = require('../../src/app');

describe('\'task\' service', () => {
  it('registered the service', () => {
    const service = app.service('task');

    assert.ok(service, 'Registered the service');
  });
});
