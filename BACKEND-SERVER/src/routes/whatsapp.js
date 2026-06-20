const express = require('express');
const router = express.Router();
const authMiddleware = require('../middleware/authMiddleware');
const whatsappController = require('../controllers/whatsappController');

router.use(authMiddleware);
router.get('/accounts', whatsappController.getAccounts);
router.post('/request-pairing', whatsappController.requestPairing);
router.get('/pairing-status/:code', whatsappController.checkPairingStatus);
router.post('/pair-confirm', whatsappController.confirmPairing);
router.delete('/accounts/:accountId', whatsappController.deleteAccount);

module.exports = router;
