module.exports = function () { // eslint-disable-line no-unused-vars
  return function preventChangeRole(hook) {
    if (!hook.params.provider) {
      return hook;
    }

    console.log(hook.data);

    if (hook.data && hook.data.role) {
        delete hook.data.role;
    }

    console.log(hook.data);

    return hook;
  };
};
