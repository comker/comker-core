define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
    'app/routers/homeRouter',
    'app/routers/permissionRouter',
    'app/routers/roleRouter',
    'app/routers/userRouter',
], function($, _, Backbone, Coke, HomeRouter, PermissionRouter, RoleRouter, UserRouter) {
    var initialize = function() {
        Coke.log.debug('Start the application');

        Coke.log.debug('Create HomeRouter object');
        var homeRouter = new HomeRouter();

        Coke.log.debug('Create PermissionRouter object');
        var permissionRouter = new PermissionRouter();

        Coke.log.debug('Create RoleRouter object');
        var roleRouter = new RoleRouter();

        Coke.log.debug('Create UserRouter object');
        var userRouter = new UserRouter();
        
        Backbone.history.start();
    };

    return {
        initialize: initialize
    };
});
