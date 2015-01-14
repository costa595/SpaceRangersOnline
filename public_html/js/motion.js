define([
    'underscore',
    'backbone',
    'views/stage',
    'views/sun',
    'views/base',
    'views/enemy',
], function (_, Backbone, stageView, sunView, BaseView, EnemyView) {

    var MotionController = function () {
        this.intervals = {};
    }

    MotionController.prototype.newInterval =  function (params) {
        if (this.intervals[params.name] !== undefined) {
            clearInterval(this.intervals[params.name]);
        }
        this.intervals[params.name] = setInterval(function () {
            params.callback.call(params.context);
        }, params.time);
    };

    MotionController.prototype.delInterval = function (names) {
        if (names instanceof Array) {
            for (i = 0; i < names.length; i++) {
                clearInterval(this.intervals[names[i]]);
            }
        }
        else {
            clearInterval(this.intervals[names]);
        }
    }

    var motionController = new MotionController();

    _.extend(motionController, Backbone.Events);
    motionController.listenTo(stageView, 'move:done', motionController.delInterval);
    motionController.listenTo(stageView, 'move:start', motionController.newInterval);
    motionController.listenTo(sunView, 'move:done', motionController.delInterval);
    motionController.listenTo(sunView, 'move:start', motionController.newInterval);
    // motionController.listenTo(BaseView, 'move:done', motionController.delInterval);
    // motionController.listenTo(BaseView, 'move:start', motionController.newInterval);

    motionController.addViewListener = function(curView) {
        this.listenTo(curView, 'move:done', this.delInterval);
        this.listenTo(curView, 'move:start', this.newInterval);
    }

    return motionController;
});
