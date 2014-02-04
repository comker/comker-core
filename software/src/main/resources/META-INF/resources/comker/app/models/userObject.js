define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
], function($, _, Backbone, Coke) {
    var UserObject = Backbone.Model.extend({
        defaults: {
            coverImage: 'app/images/user-default-cover.png',
            title: 'No title',
            author: 'Unknown',
            publishDate: 'Unknown',
            keywords: 'None'
        },
        initialize: function(){
            if (!this.get('description')) {
                this.set('description', 'Description of:' + this.get('title'));
            }
        }
    });
    return UserObject;
});


