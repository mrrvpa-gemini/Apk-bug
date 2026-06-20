module.exports = {
  apps: [{
    name: 'manta-x-backend',
    script: './src/index.js',
    instances: 1,
    exec_mode: 'fork',
    watch: false,
    max_memory_restart: '1G',
    env: { NODE_ENV: 'production', PORT: 3000 },
    log_file: './logs/combined.log',
    out_file: './logs/out.log',
    error_file: './logs/error.log',
    time: true
  }]
};
