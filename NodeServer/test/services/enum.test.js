const assert = require('assert');
const app = require('../../src/app');

describe('\'enum\' service', () => {
  it('registered the service', () => {
    const service = app.service('enum');

    assert.ok(service, 'Registered the service');
  });
});
