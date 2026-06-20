module.exports = {
  broadcastToUser: (io, userId, event, data) => {
    io.to(`user_${userId}`).emit(event, data);
  },
  broadcastAll: (io, event, data) => {
    io.emit(event, data);
  }
};
