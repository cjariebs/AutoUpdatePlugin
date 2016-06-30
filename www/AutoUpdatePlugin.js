function AutoUpdatePlugin() {
    console.log("AutoUpdatePlugin");
}

AutoUpdatePlugin.prototype.updateApp = function(callback) {
    cordova.exec(callback, function(err) {
        console.error("Unable to update app: " + JSON.stringify(err));
    }, "AutoUpdatePlugin", "updateApp", []);
};


var update = new AutoUpdatePlugin();
module.exports = update;
