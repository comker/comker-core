define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
    'app/routers/authRouter',
    'app/routers/homeRouter',
    'app/routers/permissionRouter',
    'app/routers/roleRouter',
    'app/routers/spotRouter',
    'app/routers/crewRouter',
    'app/routers/userRouter',
], function($, _, Backbone, Coke, AuthRouter, HomeRouter, PermissionRouter, RoleRouter, SpotRouter, CrewRouter, UserRouter) {
    var initialize = function() {
        Coke.log.debug('Start the application');

        Coke.log.debug('Create AuthRouter object');
        var authRouter = new AuthRouter();

        Coke.log.debug('Create HomeRouter object');
        var homeRouter = new HomeRouter();

        Coke.log.debug('Create PermissionRouter object');
        var permissionRouter = new PermissionRouter();

        Coke.log.debug('Create RoleRouter object');
        var roleRouter = new RoleRouter();

        Coke.log.debug('Create SpotRouter object');
        var spotRouter = new SpotRouter();

        Coke.log.debug('Create CrewRouter object');
        var crewRouter = new CrewRouter();

        Coke.log.debug('Create UserRouter object');
        var userRouter = new UserRouter();
        
        Backbone.history.start();
    };

    return {
        initialize: initialize
    };
});
