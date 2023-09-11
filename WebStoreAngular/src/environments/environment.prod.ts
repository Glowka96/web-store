const dotenv = require('dotenv');
dotenv.config();

export const environment = {
  production: true,
  apiBaseUrl: process.env.API_BASE_URL,
};
