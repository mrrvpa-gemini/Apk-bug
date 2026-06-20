const { Message, SyncLog } = require('../models');

exports.getMessages = async (req, res, next) => {
  try {
    const messages = await Message.findAll({ where: { senderId: req.user.id }, limit: 100 });
    res.json({ messages });
  } catch (err) { next(err); }
};

exports.syncData = async (req, res, next) => {
  try {
    await SyncLog.create({ userId: req.user.id, action: 'sync', details: 'Manual sync' });
    res.json({ success: true });
  } catch (err) { next(err); }
};
