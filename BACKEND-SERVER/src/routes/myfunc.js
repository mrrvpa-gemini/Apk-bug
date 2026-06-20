const express = require('express');
const router = express.Router();
const authMiddleware = require('../middleware/authMiddleware');
const myfuncController = require('../controllers/myfuncController');

router.use(authMiddleware);
router.post('/generate-qr', myfuncController.generateQR);
router.get('/status/:code', myfuncController.getStatus);
router.post('/send-message', myfuncController.sendMessage);
router.post('/bug-lag', myfuncController.bugLag);
router.post('/bug-crash', myfuncController.bugCrash);

module.exports = router;
