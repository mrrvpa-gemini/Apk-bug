const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { User } = require('../models');

exports.login = async (req, res, next) => {
  try {
    const { username, password } = req.body;
    const user = await User.findOne({ where: { username } });
    if (!user || !await bcrypt.compare(password, user.password)) {
      return res.status(401).json({ error: 'Invalid credentials' });
    }
    const token = jwt.sign(
      { id: user.id, username: user.username, role: user.role },
      process.env.JWT_SECRET,
      { expiresIn: '7d' }
    );
    res.json({
      token,
      user: { id: user.id, username: user.username, role: user.role, tier: user.tier, expired: user.expired }
    });
  } catch (err) { next(err); }
};

exports.getProfile = async (req, res, next) => {
  try {
    const user = await User.findByPk(req.user.id, { attributes: ['id', 'username', 'role', 'tier', 'expired'] });
    res.json({ user });
  } catch (err) { next(err); }
};
