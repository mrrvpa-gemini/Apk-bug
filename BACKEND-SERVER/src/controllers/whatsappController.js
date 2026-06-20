const { WhatsAppAccount, PairingCode } = require('../models');
const MyfuncManager = require('../services/MyfuncManager');
const crypto = require('crypto');

exports.getAccounts = async (req, res, next) => {
  try {
    const accounts = await WhatsAppAccount.findAll({ where: { userId: req.user.id } });
    res.json({ accounts });
  } catch (err) { next(err); }
};

exports.requestPairing = async (req, res, next) => {
  try {
    const pairingCode = crypto.randomBytes(4).toString('hex').toUpperCase();
    const expiresIn = 300;
    await PairingCode.create({
      code: pairingCode, userId: req.user.id,
      expiresAt: new Date(Date.now() + expiresIn * 1000)
    });
    const qrData = await MyfuncManager.generateQR(req.user.id, pairingCode);
    res.json({ pairingCode, expiresIn, qrData });
  } catch (err) { next(err); }
};

exports.checkPairingStatus = async (req, res, next) => {
  try {
    const { code } = req.params;
    const pairing = await PairingCode.findOne({ where: { code } });
    if (!pairing) return res.json({ success: false, message: 'Invalid code' });
    if (pairing.expiresAt < new Date()) return res.json({ success: false, message: 'Expired' });
    if (pairing.status === 'completed') {
      const account = await WhatsAppAccount.findByPk(pairing.accountId);
      return res.json({ success: true, account });
    }
    res.json({ success: false, status: pairing.status });
  } catch (err) { next(err); }
};

exports.confirmPairing = async (req, res, next) => {
  try {
    res.json({ success: true });
  } catch (err) { next(err); }
};

exports.deleteAccount = async (req, res, next) => {
  try {
    const { accountId } = req.params;
    await WhatsAppAccount.destroy({ where: { id: accountId, userId: req.user.id } });
    res.json({ success: true });
  } catch (err) { next(err); }
};
