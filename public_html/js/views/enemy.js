define([
    'backbone',
    'resources',
    'models/enemy'
], function(Backbone, resources, EnemyModel) {

    var EnemyView = Backbone.View.extend({
        tagName: 'canvas',
        className: 'enemy',
        id: 'ship',
        // model: EnemyModel,
        initialize: function() {
            this.inited = false;
            // this.listenTo(this.model, 'change', this.move);
            this.listenTo(this.model, 'enemyMove:start', this.move);
            this.listenTo(this.model, 'updateView', this.render);


            this.ctx = this.el.getContext('2d');
            // resources.load(['/images/ship2.png']);
            resources.load(['/images/ship2.png']);
            this.el.width = $('body').width();
            this.el.height = $('body').height();
            this.inMove = false;
            this.loaded = false;
            var that = this;
            resources.onReady(function() {
                that.loaded = true;
                that.image = resources.get('/images/ship2.png');
            })
        },
        render: function() {
            var that = this;
            this.ctx.strokeRect(10, 10, 70, 70);
            this.ctx.clearRect(-that.el.width / 2, -that.el.height / 2, this.el.width, this.el.height);
            if (!this.model.inGame && this.inited) {
                this.trigger('move:done', ['moveEnemy-' + this.model.get('login'), 'move']);
                return this;
            }

            if (this.inited) {
                if (this.haveOwnDelta) {
                    var newAngle = Math.atan2(this.targetX, this.targetY) + Math.PI / 4;
                    var tang = Math.tan(newAngle);
                    this.ctx.drawImage(this.image, this.model.getPosition().x, this.model.getPosition().y, 70, 70);
                } else {
                    this.ctx.drawImage(this.image, this.model.getPosition().x, this.model.getPosition().y, 70, 70);
                }
                if (this.model.marked) {
                    this.ctx.strokeRect(this.model.getPosition().x, this.model.getPosition().y, 70, 70);
                }
            } else {
                if (this.loaded) {
                    this.ctx.translate(this.el.width / 2, this.el.height / 2);
                    this.ctx.drawImage(this.image, this.model.getPosition().x, this.model.getPosition().y, 70, 70);
                    this.model.inGame = true;
                    this.inited = true;
                } else {
                    this.image = resources.get('/images/ship2.png');
                    if (this.image) {
                        this.ctx.clearRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
                        this.image = resources.get('/images/ship2.png');
                        this.ctx.translate(this.el.width / 2, this.el.height / 2);
                        this.ctx.drawImage(this.image, this.model.getPosition().x, this.model.getPosition().y, 70, 70);
                        this.model.inGame = true;
                        this.inited = true;
                    } else {
                        resources.onReady(function() {
                            if (!that.inited) {
                                that.ctx.clearRect(0, 0, that.ctx.canvas.width, that.ctx.canvas.height);
                                that.image = resources.get('/images/ship2.png');
                                that.ctx.translate(that.el.width / 2, that.el.height / 2);
                                that.ctx.drawImage(that.image, that.model.getPosition().x, that.model.getPosition().y, 70, 70);
                                that.inited = true;
                                that.model.inGame = true;
                                that.loaded = true;
                            }
                            
                        });
                    }

                }
            }
            return this;
        },

        move: function() {
            //Сколько осталось лететь
            //Так как this.model.get('x') устанавливается в начале перемещения, он ен подходит для того, чтобы считать совместный полет
            //palyerDeltha
            this.targetX = this.model.palyerDeltha.x
            this.targetY = this.model.palyerDeltha.y;
            this.haveOwnDelta = false;
            if (Math.abs(this.model.ownDeltha.x) > 2) {
                this.targetX += this.model.ownDeltha.x;
                this.haveOwnDelta = true;
            }
            if (Math.abs(this.model.ownDeltha.y) > 2) {
                this.targetY += this.model.ownDeltha.y;
                this.haveOwnDelta = true;
            }

            this.xSpeed = this.targetX;
            this.ySpeed = this.targetY;
            if (this.model.inGame) {
                this.trigger('move:start', {
                    context: this,
                    callback: this.motion,
                    time: 10,
                    name: 'moveEnemy-' + this.model.get('login')
                });
            }
        },
        rotate: function() {
            var ctx = this.ctx;
            // ctx.clearRect(-35, -35, 70, 70);
            ctx.save();
            var newAngle = Math.atan2(this.targetX, this.targetY) + Math.PI / 2;
            // this.angle -= 2;
            ctx.rotate(newAngle);
            // ctx.rotate(Math.PI / 4);
            // ctx.drawImage(this.image, -35, -35, 70, 70);
            // this.render();
            // ctx.restore();
        },
        motion: function() {
            if (!this.targetX || !this.targetY) {
                this.targetX = 0;
                this.targetY = 0;
                this.trigger('move:done', ['moveEnemy-' + this.model.get('login'), 'move']);
                return;
            }



            // this.ctx.clearRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
            // this.ctx.drawImage(this.image, this.model.getPosition().x, this.model.getPosition().y, 70, 70);
            // this.ctx.restore();
            if (Math.abs(this.xSpeed) >= Math.abs(this.ySpeed)) {
                this.targetX -= this.xSpeed / Math.abs(this.xSpeed) * 2;
                this.targetY -= this.ySpeed / Math.abs(this.ySpeed) * Math.abs(this.ySpeed / this.xSpeed) * 2;
                this.model.changeX(this.xSpeed / Math.abs(this.xSpeed) * 2);
                this.model.changeY(this.ySpeed / Math.abs(this.ySpeed) * Math.abs(this.ySpeed / this.xSpeed) * 2);
            } else {
                this.model.changeY(this.ySpeed / Math.abs(this.ySpeed) * 2);
                this.targetY -= this.ySpeed / Math.abs(this.ySpeed) * 2;
                this.model.changeX(this.xSpeed / Math.abs(this.xSpeed) * Math.abs(this.xSpeed / this.ySpeed) * 2);
                this.targetX -= this.xSpeed / Math.abs(this.xSpeed) * Math.abs(this.xSpeed / this.ySpeed) * 2;
            }
            if ((Math.abs(this.targetX) < 2) && (Math.abs(this.targetY) < 2)) {
                this.model.palyerDeltha.x = 0;
                this.model.palyerDeltha.y = 0;
                this.model.ownDeltha.x = 0;
                this.model.ownDeltha.y = 0;
                this.targetX = 0;
                this.targetY = 0;
                this.trigger('move:done', ['moveEnemy-' + this.model.get('login'), 'move']);
                // this.model.set({"inMove": false})
            }
                        this.render();
        }
    });

    return EnemyView;

});