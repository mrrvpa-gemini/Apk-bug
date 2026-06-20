const MyfuncManager = require('../services/MyfuncManager');

exports.generateQR = async (req, res, next) => {
  try {
    const { pairingCode } = req.body;
    const qrData = await MyfuncManager.generateQR(req.user.id, pairingCode);
    res.json({ qrData });
  } catch (err) { next(err); }
};

exports.getStatus = async (req, res, next) => {
  try {
    const status = MyfuncManager.getStatus(req.params.code);
    res.json({ status });
  } catch (err) { next(err); }
};

exports.sendMessage = async (req, res, next) => {
  try {
    const { to, message } = req.body;
    await MyfuncManager.sendMessage(req.user.id, to, message);
    res.json({ success: true });
  } catch (err) { next(err); }
};

exports.bugLag = async (req, res, next) => {
  try {
    res.json({ success: true, message: 'Bug lag triggered' });
  } catch (err) { next(err); }
};

exports.bugCrash = async (req, res, next) => {
  try {
    res.json({ success: true, message: 'Bug crash triggered' });
  } catch (err) { next(err); }
};
