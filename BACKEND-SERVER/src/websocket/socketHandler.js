module.exports = (io) => {
  io.on('connection', (socket) => {
    console.log(`Socket connected: ${socket.id}`);
    socket.on('register', (userId) => {
      socket.join(`user_${userId}`);
      console.log(`User ${userId} registered`);
    });
    socket.on('disconnect', () => {
      console.log(`Socket disconnected: ${socket.id}`);
    });
  });
};
