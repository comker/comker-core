require.config({
    paths: {
        jquery: 'lib/jquery/1.10.2/jquery-min',
        underscore: 'lib/underscore/1.5.2-amdjs/underscore-min',
        backbone: 'lib/backbone/1.1.0-amdjs/backbone',
        logging: 'app/utils/loggingStarter',
        log4javascript: 'lib/log4javascript/1.4.6/log4javascript',
        bootstrap: 'lib/bootstrap/js/bootstrap.min',
        i18n: 'lib/i18next/1.7.1/i18next'
    },
    shim: {
        'i18n' : {
            deps: ['jquery'],
            exports: 'i18n'
        }
    }
});

require([
    'startup',
], function(App){
    App.initialize();
});