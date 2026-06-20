require('dotenv').config();

module.exports = {
  PORT: process.env.PORT || 3000,
  JWT_SECRET: process.env.JWT_SECRET || 'mantax_gacor_secret',
  DB_HOST: process.env.DB_HOST || 'localhost',
  DB_NAME: process.env.DB_NAME || 'mantax_db',
  DB_USER: process.env.DB_USER || 'root',
  DB_PASS: process.env.DB_PASS || '',
  TELEGRAM_BOT_TOKEN: process.env.TELEGRAM_BOT_TOKEN,
  TELEGRAM_OWNER_ID: process.env.TELEGRAM_OWNER_ID
};
