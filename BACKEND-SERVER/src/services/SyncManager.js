const { SyncLog, Message } = require('../models');

class SyncManager {
  async logSync(userId, action, details = '') {
    return SyncLog.create({ userId, action, details });
  }

  async getMessages(userId, limit = 100) {
    return Message.findAll({ where: { senderId: userId }, limit, order: [['createdAt', 'DESC']] });
  }
}

module.exports = new SyncManager();
