const assert = require('assert');
const app = require('../.././/app');

describe('\'build-version\' service', () => {
  it('registered the service', () => {
    const service = app.service('build-version');

    assert.ok(service, 'Registered the service');
  });
});
