const { PairingCode } = require('../models');

class PairingManager {
  async createPairing(userId, code, expiresIn = 300) {
    return PairingCode.create({
      code, userId,
      expiresAt: new Date(Date.now() + expiresIn * 1000)
    });
  }

  async getPairing(code) {
    return PairingCode.findOne({ where: { code } });
  }

  async updateStatus(code, status, accountId = null) {
    const update = { status };
    if (accountId) update.accountId = accountId;
    return PairingCode.update(update, { where: { code } });
  }
}

module.exports = new PairingManager();
