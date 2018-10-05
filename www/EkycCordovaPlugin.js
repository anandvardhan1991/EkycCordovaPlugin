var exec = require('cordova/exec');

var otpVerification = (args, success, error) =>{
    exec(success, error, 'EkycCordovaPlugin', 'otpVerification', [args]);
};

var irisVerification = (args, success, error) =>{
    exec(success, error, 'EkycCordovaPlugin', 'irisVerification', [args]);
};

var fingerPrintVerification = (args, success, error) =>{
    exec(success, error, 'EkycCordovaPlugin', 'fingerPrintVerification', [args]);
};

module.exports = {otpVerification , irisVerification , fingerPrintVerification};
