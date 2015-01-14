define([
    'jquery',
    'backbone',
    'resources',
    'views/ship',
    'models/ship',
    'models/stage'
], function ($, Backbone, resources, shipView, shipModel, stageModel) {
    var StageView = Backbone.View.extend({
        tagName: 'canvas',
        id: 'stage',
        className: 'stage',
        model: stageModel,
        initialize: function () {
            this.listenTo(stageModel, 'change', this.move);
            this.ctx = this.el.getContext('2d');
            resources.load(['/images/space1.jpg']);
        },
        render: function() {
            this.el.width = $('body').width();
            this.el.height = $('body').height();
            this.model.xpos = 1500 - (this.el.width / 2);
            this.model.ypos = 1500 - (this.el.height / 2);
            var that = this;
            resources.onReady(function () {
                try {
                    that.ctx.drawImage(resources.get('/images/space1.jpg'), that.model.xpos, that.model.ypos, that.el.width, that.el.height, 0, 0, that.el.width, that.el.height);
                } catch (e) {}
                
            });
            this.ctx.stroke();
            return this;
        },
        move: function (model) {
            this.trigger('move:start', {context: this, callback: this.motion, time: 40, name: 'move'});
        },
        motion: function () {
            this.ctx.clearRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
            this.ctx.drawImage(resources.get('/images/space1.jpg'), this.model.xpos, this.model.ypos, this.el.width, this.el.height, 0, 0, this.el.width, this.el.height);
            this.ctx.restore();
            this.model.calculatePosition();
        }
    });
    return new StageView();
});