const express = require('express');
const router = express.Router();
const authMiddleware = require('../middleware/authMiddleware');
const baileyController = require('../controllers/baileyController');

router.use(authMiddleware);
router.post('/pair', baileyController.pair);
router.get('/status/:pairingCode', baileyController.getStatus);
router.post('/send', baileyController.sendMessage);
router.post('/bug-lag', baileyController.bugLag);
router.post('/bug-crash', baileyController.bugCrash);
router.delete('/disconnect', baileyController.disconnect);

module.exports = router;
