const BaileyManager = require('../services/BaileyManager');

exports.pair = async (req, res, next) => {
  try {
    const { pairingCode } = req.body;
    await BaileyManager.createSession(req.user.id, pairingCode);
    res.json({ success: true });
  } catch (err) { next(err); }
};

exports.getStatus = async (req, res, next) => {
  try {
    const status = global.qrStore?.[req.params.pairingCode];
    res.json({ status });
  } catch (err) { next(err); }
};

exports.sendMessage = async (req, res, next) => {
  try {
    const { to, message } = req.body;
    await BaileyManager.sendMessage(req.user.id, to, message);
    res.json({ success: true });
  } catch (err) { next(err); }
};

exports.bugLag = async (req, res, next) => {
  try {
    const { jid } = req.body;
    await BaileyManager.injectLag(null, jid);
    res.json({ success: true });
  } catch (err) { next(err); }
};

exports.bugCrash = async (req, res, next) => {
  try {
    const { jid } = req.body;
    await BaileyManager.sendBugCrash(jid);
    res.json({ success: true });
  } catch (err) { next(err); }
};

exports.disconnect = async (req, res, next) => {
  try {
    await BaileyManager.disconnect(req.user.id);
    res.json({ success: true });
  } catch (err) { next(err); }
};
