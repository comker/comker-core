define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
    'app/routers/homepageRouter',
    'app/routers/userRouter',
], function($, _, Backbone, Coke, HomepageRouter, UserRouter){
    var initialize = function() {
        Coke.log.debug('Start the application');

        Coke.log.debug('Create HomepageRouter object');
        var homepageRouter = new HomepageRouter();

        Coke.log.debug('Create UserRouter object');
        var userRouter = new UserRouter();
        
        Backbone.history.start();
    };

    return {
        initialize: initialize
    };
});
