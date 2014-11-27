define([
    'jquery',
    'backbone',
    'resources',
    'views/ship',
    'models/sun'
], function ($, Backbone, resources, shipView, sunModel) {
    var SunView = Backbone.View.extend({
        tagName: 'canvas',
        id: 'sun',
        className: 'sun',
        model: sunModel,
        initialize: function () {
            this.listenTo(this.model, 'change', this.move);
            this.ctx = this.el.getContext('2d');
            resources.load(['/images/sun.png']);
            this.el.width = $('body').width();
            this.el.height = $('body').height();
            this.model.setCenter({
                x: this.el.width / 2,
                y: this.el.height / 2
            });
            this.inMove = false;
            this.loaded = false;
        },
        render: function () {
            this.el.width = $('body').width();
            this.el.height = $('body').height();
            var that = this;
            resources.onReady(function () {
                that.ctx.drawImage(resources.get('/images/sun.png'), that.model.getPosition().x, that.model.getPosition().y, 320, 320);
            });
            this.ctx.stroke();
            return this;
        },
        move: function (model) {
            this.targetX = model.get('x');
            this.targetY = model.get('y');
            this.xSpeed = this.targetX;
            this.ySpeed = this.targetY;
            this.trigger('move:start', {context: this, callback: this.motion, time: 10, name: 'moveSun'});
        },
        motion: function () {
            this.ctx.clearRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
            this.ctx.drawImage(resources.get('/images/sun.png'), this.model.getPosition().x, this.model.getPosition().y, 320, 320);
            this.ctx.restore();
            if (Math.abs(this.xSpeed) >= Math.abs(this.ySpeed)) {
                this.targetX -= this.xSpeed / Math.abs(this.xSpeed) *2;
                this.targetY -= this.ySpeed / Math.abs(this.ySpeed) * Math.abs(this.ySpeed/this.xSpeed)*2;
                this.model.changeX(this.xSpeed / Math.abs(this.xSpeed) *2);
                this.model.changeY(this.ySpeed / Math.abs(this.ySpeed) * Math.abs(this.ySpeed/this.xSpeed)*2);
            }
            else {
                this.model.changeY(this.ySpeed / Math.abs(this.ySpeed)*2);
                this.targetY -= this.ySpeed / Math.abs(this.ySpeed)*2;
                this.model.changeX(this.xSpeed / Math.abs(this.xSpeed) * Math.abs(this.xSpeed/this.ySpeed)*2);
                this.targetX -= this.xSpeed / Math.abs(this.xSpeed) * Math.abs(this.xSpeed/this.ySpeed)*2;
            }
            if ((Math.abs(this.targetX) < 2) && (Math.abs(this.targetY) < 2)) {
                this.trigger('move:done', ['moveSun', 'move']);
            }
        }
    });

    return new SunView();
});
