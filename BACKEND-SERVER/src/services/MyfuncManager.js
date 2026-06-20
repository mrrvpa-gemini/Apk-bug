const { default: makeWASocket, useMultiFileAuthState } = require('@whiskeysockets/baileys');
const QRCode = require('qrcode');
const path = require('path');
const fs = require('fs');

class MyfuncManager {
  constructor() {
    this.sessions = new Map();
    this.pairingCodes = new Map();
  }

  async generateQR(userId, pairingCode) {
    const sessionDir = path.join(__dirname, '../../sessions', userId);
    fs.mkdirSync(sessionDir, { recursive: true });
    const { state, saveCreds } = await useMultiFileAuthState(sessionDir);
    const sock = makeWASocket({
      auth: state, printQRInTerminal: false,
      browser: ['MantaX', 'Chrome', '1.0']
    });
    this.sessions.set(userId, { sock, saveCreds, pairingCode });
    return new Promise((resolve, reject) => {
      sock.ev.on('connection.update', async (update) => {
        const { qr, connection, lastDisconnect } = update;
        if (qr) {
          const qrDataUrl = await QRCode.toDataURL(qr);
          this.pairingCodes.set(pairingCode, { qr, sock, userId, status: 'waiting' });
          resolve(qrDataUrl);
        }
        if (connection === 'open') {
          this.pairingCodes.set(pairingCode, { ...this.pairingCodes.get(pairingCode), status: 'connected' });
          global.io?.to(`user_${userId}`).emit('pairing_success', { pairingCode, number: sock.user.id });
        }
        if (connection === 'close') {
          const shouldReconnect = lastDisconnect?.error?.output?.statusCode !== 401;
          if (shouldReconnect) this.generateQR(userId, pairingCode);
        }
      });
      sock.ev.on('creds.update', saveCreds);
    });
  }

  getStatus(pairingCode) {
    return this.pairingCodes.get(pairingCode) || null;
  }

  async sendMessage(userId, to, message) {
    const session = this.sessions.get(userId);
    if (!session) throw new Error('Session not found');
    await session.sock.sendMessage(to, { text: message });
  }

  async disconnect(userId) {
    const session = this.sessions.get(userId);
    if (session) {
      await session.sock.logout();
      this.sessions.delete(userId);
    }
  }
}

module.exports = new MyfuncManager();
