const sequelize = require('../config/database');
const { DataTypes } = require('sequelize');

const User = sequelize.define('User', {
  id: { type: DataTypes.STRING(36), primaryKey: true, defaultValue: DataTypes.UUIDV4 },
  username: { type: DataTypes.STRING(50), unique: true, allowNull: false },
  password: { type: DataTypes.STRING(255), allowNull: false },
  role: { type: DataTypes.ENUM('owner', 'admin', 'user'), defaultValue: 'user' },
  tier: { type: DataTypes.STRING(20), defaultValue: 'free' },
  expired: { type: DataTypes.BIGINT, defaultValue: 0 },
  telegramId: { type: DataTypes.STRING(50) }
});

const WhatsAppAccount = sequelize.define('WhatsAppAccount', {
  id: { type: DataTypes.STRING(36), primaryKey: true, defaultValue: DataTypes.UUIDV4 },
  userId: { type: DataTypes.STRING(36), allowNull: false },
  number: { type: DataTypes.STRING(20), allowNull: false },
  status: { type: DataTypes.ENUM('connected', 'offline', 'error'), defaultValue: 'offline' },
  lastActive: { type: DataTypes.DATE, defaultValue: DataTypes.NOW },
  sessionData: { type: DataTypes.TEXT }
});

const PairingCode = sequelize.define('PairingCode', {
  id: { type: DataTypes.STRING(36), primaryKey: true, defaultValue: DataTypes.UUIDV4 },
  code: { type: DataTypes.STRING(8), unique: true, allowNull: false },
  userId: { type: DataTypes.STRING(36), allowNull: false },
  status: { type: DataTypes.ENUM('waiting', 'scanned', 'connected', 'expired'), defaultValue: 'waiting' },
  expiresAt: { type: DataTypes.DATE, allowNull: false },
  accountId: { type: DataTypes.STRING(36) }
});

const Message = sequelize.define('Message', {
  id: { type: DataTypes.STRING(36), primaryKey: true, defaultValue: DataTypes.UUIDV4 },
  senderId: { type: DataTypes.STRING(36), allowNull: false },
  recipient: { type: DataTypes.STRING(50), allowNull: false },
  content: { type: DataTypes.TEXT, allowNull: false },
  status: { type: DataTypes.ENUM('pending', 'sent', 'delivered', 'failed'), defaultValue: 'pending' },
  type: { type: DataTypes.STRING(20), defaultValue: 'text' }
});

const SyncLog = sequelize.define('SyncLog', {
  id: { type: DataTypes.STRING(36), primaryKey: true, defaultValue: DataTypes.UUIDV4 },
  userId: { type: DataTypes.STRING(36), allowNull: false },
  action: { type: DataTypes.STRING(50), allowNull: false },
  details: { type: DataTypes.TEXT }
});

User.hasMany(WhatsAppAccount, { foreignKey: 'userId' });
WhatsAppAccount.belongsTo(User, { foreignKey: 'userId' });

module.exports = { sequelize, User, WhatsAppAccount, PairingCode, Message, SyncLog };
