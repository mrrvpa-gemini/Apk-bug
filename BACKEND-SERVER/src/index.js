const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const { createServer } = require('http');
const { Server } = require('socket.io');
require('dotenv').config();

const authRoutes = require('./routes/auth');
const whatsappRoutes = require('./routes/whatsapp');
const myfuncRoutes = require('./routes/myfunc');
const baileyRoutes = require('./routes/bailey');
const syncRoutes = require('./routes/sync');
const telegramRoutes = require('./routes/telegram');

const { errorHandler } = require('./middleware/errorHandler');
const logger = require('./config/logger');

const app = express();
const httpServer = createServer(app);
const io = new Server(httpServer, {
  cors: { origin: '*' },
  transports: ['websocket', 'polling']
});

global.io = io;

app.use(helmet());
app.use(cors());
app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ extended: true }));

app.use('/api/v1/auth', authRoutes);
app.use('/api/v1/whatsapp', whatsappRoutes);
app.use('/api/v1/myfunc', myfuncRoutes);
app.use('/api/v1/bailey', baileyRoutes);
app.use('/api/v1/sync', syncRoutes);
app.use('/api/v1/telegram', telegramRoutes);

io.on('connection', (socket) => {
  logger.info(`Client connected: ${socket.id}`);
  socket.on('register', (userId) => {
    socket.join(`user_${userId}`);
    logger.info(`User ${userId} registered`);
  });
  socket.on('disconnect', () => {
    logger.info(`Client disconnected: ${socket.id}`);
  });
});

app.use(errorHandler);

const PORT = process.env.PORT || 3000;
httpServer.listen(PORT, () => {
  logger.info(`Manta'X Server running on port ${PORT}`);
});
