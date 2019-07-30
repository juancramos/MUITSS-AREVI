/* eslint-disable no-unused-vars */
class EnumService {
  constructor (options) {
    this.options = options || {};
  }

  async find(params) {
    const { service, path } = params.query;
    console.log(service, path);

    console.log(Object.keys(this.app.service(service).Model.attributes[path]));

    
    const values = await this.app.service(service).Model.attributes[path].values;

    return values || [];
  }

  setup(app) {
    this.app = app;
  }
}

module.exports = function (options) {
  return new EnumService(options);
};

module.exports.Service = EnumService;
