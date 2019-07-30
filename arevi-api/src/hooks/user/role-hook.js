module.exports = function () { // eslint-disable-line no-unused-vars
  return function preventChangeRole(hook) {
    if (!hook.params.provider) {
      return hook;
    }

    if (hook.data && hook.data.role) {
        delete hook.data.role;
    }

    return hook;
  };
};
