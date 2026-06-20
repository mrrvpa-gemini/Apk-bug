const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');

router.post('/', authController.login);
router.get('/profile', require('../middleware/authMiddleware'), authController.getProfile);

module.exports = router;
