require.config({
    paths: {
        jquery: 'lib/jquery/1.10.2/jquery-1.10.2.min',
        underscore: 'lib/underscore/1.5.2-amdjs/underscore-min',
        backbone: 'lib/backbone/1.1.0-amdjs/backbone',
        logging: 'app/utils/loggingStarter',
        log4javascript: 'lib/log4javascript/1.4.6/log4javascript',
        bootstrap: 'lib/bootstrap/js/bootstrap.min'
    }
});

require([
    'startup',
], function(App){
    App.initialize();
});