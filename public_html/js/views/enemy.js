define([
    'backbone',
    'resources',
    'models/enemy'
], function (Backbone, resources, EnemyModel) {
    
    var EnemyView = Backbone.View.extend({
        tagName: 'canvas',
        className: 'enemy',
        id: 'ship',
        model: EnemyModel,
        initialize: function () {
            this.inited = false;
            this.listenTo(this.model, 'change', this.move);
            this.listenTo(this.model, 'modelInited', this.render);


            this.ctx = this.el.getContext('2d');
            // resources.load(['/images/ship2.png']);
             resources.load(['/images/ship2.png']);
            this.el.width = $('body').width();
            this.el.height = $('body').height();
            this.inMove = false;
            this.loaded = false;
            var that = this;
            resources.onReady(function () {
                that.loaded = true;
                that.image = resources.get('/images/ship2.png');
            })
        },
        render: function () {
            var that = this;
            if (!this.model.get('inGame') && this.inited) {
                this.trigger('move:done', ['moveEnemy', 'move']);
                return this;
            } 
            
            if (this.inited) {
                this.ctx.clearRect(-that.el.width / 2, -that.el.height / 2, this.el.width, this.el.height);
                this.ctx.drawImage(this.image, this.model.getPosition().x, this.model.getPosition().y, 70, 70);
            } else {
                if (this.loaded) {
                    that.ctx.translate(that.el.width / 2, that.el.height / 2);
                    that.ctx.drawImage(that.image, that.model.x, that.model.y, 70, 70);
                    that.inited = true;
                } else {
                    resources.onReady(function () {
                        that.ctx.clearRect(0, 0, that.ctx.canvas.width, that.ctx.canvas.height);
                        that.image = resources.get('/images/ship2.png');
                        that.ctx.translate(that.el.width / 2, that.el.height / 2);
                        that.ctx.drawImage(that.image, that.model.x, that.model.y, 70, 70);
                        that.inited = true;
                    });
                }
            }
            return this;
        },

        move: function (model) {
            // var modelState = model.getTargetPosition()
            this.targetX = model.get('x');
            this.targetY = model.get('y');
            // console.log('enemy move targetX targetY', this.targetX, this.targetY)
            this.xSpeed = this.targetX;
            this.ySpeed = this.targetY;
            if (model.get('inGame')) {
               // model.set({"inMove": true})
                this.trigger('move:start', {context: this, callback: this.motion, time: 10, name: 'moveEnemy'});
            }
        },
        motion: function () {
            if (!this.targetX || !this.targetY) {
                this.trigger('move:done', ['moveEnemy', 'move']);
                return;
            }
            this.render();
            // this.ctx.clearRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
            // this.ctx.drawImage(this.image, this.model.getPosition().x, this.model.getPosition().y, 70, 70);
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
                // console.log('enemy motion move done < 2')
                this.trigger('move:done', ['moveEnemy', 'move']);
                // this.model.set({"inMove": false})
            }
        }
    });

    return new EnemyView();

});
