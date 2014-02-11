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
        var app = app || {};

        Coke.log.debug('Start the application');

        Coke.log.debug('Create AuthRouter object');
        app.authRouter = new AuthRouter();

        Coke.log.debug('Create HomeRouter object');
        app.homeRouter = new HomeRouter();

        Coke.log.debug('Create PermissionRouter object');
        app.permissionRouter = new PermissionRouter();

        Coke.log.debug('Create RoleRouter object');
        app.roleRouter = new RoleRouter();

        Coke.log.debug('Create SpotRouter object');
        app.spotRouter = new SpotRouter();

        Coke.log.debug('Create CrewRouter object');
        app.crewRouter = new CrewRouter();

        Coke.log.debug('Create UserRouter object');
        app.userRouter = new UserRouter();
        
        Backbone.history.start();
    };

    return {
        initialize: initialize
    };
});
