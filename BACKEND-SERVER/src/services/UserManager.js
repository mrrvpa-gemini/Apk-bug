const { User } = require('../models');
const bcrypt = require('bcryptjs');

class UserManager {
  async createUser(username, password, role = 'user') {
    const hashed = await bcrypt.hash(password, 10);
    return User.create({ username, password: hashed, role });
  }

  async getUserById(id) {
    return User.findByPk(id);
  }
}

module.exports = new UserManager();
