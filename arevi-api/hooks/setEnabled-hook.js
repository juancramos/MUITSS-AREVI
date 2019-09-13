module.exports = function () { // eslint-disable-line no-unused-vars
    return async function setEnabled(hook) {
        if (!hook.params.provider) {
            return hook;
        }

        if (hook.data && hook.data.userId) {
            const update = await hook.app.service(hook.path).find({
                query: {
                    userId: hook.data.userId,
                    enabled: 1
                }
            });
            if (update && update.data && update.data.length > 0) {
                update.data.map(async x => {
                    await hook.app.service(hook.path).patch(x.id, {
                        enabled: 0
                    });
                });
            }
        }

        return hook;
    };
};