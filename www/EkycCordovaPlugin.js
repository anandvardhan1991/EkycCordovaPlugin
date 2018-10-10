var exec = require('cordova/exec');


var irisVerification = (args, success, error) =>{
    exec(success, error, 'EkycCordovaPlugin', 'irisVerification', [args]);
};

var fingerPrintVerification = (args, success, error) =>{
    exec(success, error, 'EkycCordovaPlugin', 'fingerPrintVerification', [args]);
};

module.exports = {irisVerification , fingerPrintVerification};
