const express = require('express');
const router = express.Router();
const telegramController = require('../controllers/telegramController');

router.post('/webhook', telegramController.webhook);
router.get('/status', telegramController.getStatus);

module.exports = router;
