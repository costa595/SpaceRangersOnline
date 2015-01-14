require.config({
    paths: {
        'jquery': 'lib/jquery',
        'underscore': 'lib/underscore',
        'backbone': 'lib/backbone',
        'phaser': 'lib/phaser.min',
        'baron': 'lib/baron.min',
        'baron_int': 'lib/baron_int',
        'dragLib': 'lib/backbone-draganddrop-delegation.min',
        'almond': 'lib/almond'
    },
    shim: {
        'backbone': {
            deps: ['underscore', 'jquery'],
            exports: 'Backbone'
        },
        'dragLib': {
            // deps: ['underscore', 'jquery'],
            exports: 'dragLib'
        },
        'underscore': {
            exports: '_'
        },
        'phaser': {
            exports: 'Phaser'
        },
        'baron': {
            exports: 'Baron'
        }
    }
});

require(['main']);
