exports.webhook = async (req, res, next) => {
  try {
    res.json({ ok: true });
  } catch (err) { next(err); }
};

exports.getStatus = async (req, res, next) => {
  try {
    res.json({ status: 'online', bot: 'MantaXBot' });
  } catch (err) { next(err); }
};
