define([
    'log4javascript',
    'bootstrap',
], function(log4js, Bootstrap) {
    
    var tool = {};

    //--------------------------------------------------- Logging Initialization
    var consoleAppender = new log4javascript.BrowserConsoleAppender();
    var pl = new log4javascript.PatternLayout("%d{HH:mm:ss,SSS} %-5p - %m{1}%n");
    consoleAppender.setLayout(pl);

    var rootLogger = log4javascript.getRootLogger();
    rootLogger.addAppender(consoleAppender);
    rootLogger.setLevel(log4javascript.Level.ALL);
    
    tool.log = rootLogger;

    //--------------------------------------------------------- Template Manager
    tool.TemplateManager = {
        templates: {},

        get: function(id, callback, flag){
            var template = this.templates[id];

            if (template) {
                callback(template);
            } else {
                var that = this;
                $.get("app/templates/" + id + ".tmpl", function(tmpl) {
                    var _result;
                    if (flag == undefined || flag == 'template') {
                        _result = _.template(tmpl);
                    } else {
                        //_result = $('<div/>').append(tmpl).html();
                        _result = _.template(tmpl, {});
                    }
                    that.templates[id] = _result;
                    callback(_result);
                }, "html"); // should use "html" datatype
            }
        }
    }

    //------------------------------------------------------------ block builder
    tool.alert = function(msg, type) {
        type = (type == 'success') ? 'alert-success' :
            ((type == 'error') ? 'alert-error' : 'alert-info');
        return $('<div>', {'class': 'alert ' + type}).append(msg);
    }

    //-------------------------------------------------------- utility functions
    tool.isDefined = function(x) {
        var undefined;
        return x !== undefined;
    }

    //------------------------------------------------------- i18n configuration
    tool.i18nConfig = {
        lang: 'en',
        debug: true,
        fallbackLng: false,
        load:'unspecific',
        resGetPath: "app/locales/__lng__/__ns__.json"
    }

    //------------------------------------------------------------------- Return
    return tool;
});
