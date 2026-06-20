const { WhatsAppAccount } = require('../models');

class WhatsAppManager {
  async createAccount(userId, number, sessionData = null) {
    return WhatsAppAccount.create({ userId, number, status: 'connected', sessionData });
  }

  async getAccounts(userId) {
    return WhatsAppAccount.findAll({ where: { userId } });
  }

  async updateStatus(id, status) {
    return WhatsAppAccount.update({ status, lastActive: new Date() }, { where: { id } });
  }

  async deleteAccount(id, userId) {
    return WhatsAppAccount.destroy({ where: { id, userId } });
  }
}

module.exports = new WhatsAppManager();
