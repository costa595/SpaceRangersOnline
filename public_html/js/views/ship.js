define([
    'jquery',
    'backbone',
    'resources',
    'models/ship',
    'models/sun'
], function ($, Backbone, resources, shipModel, SunModel) {

    var ShipView = Backbone.View.extend({
        tagName: 'canvas',
        id: 'ship',
        className: 'ship',
        model: shipModel,
        initialize: function () {
            // $(document).bind('keyup', this.keyPress);
            this.ctx = this.el.getContext('2d');
            resources.load(['/images/ship.gif']);
            this.el.width = $('body').width();
            this.el.height = $('body').height();
            this.model.setCenter({
                x: this.el.width / 2,
                y: this.el.height / 2
            });
            this.shipPos = {
                x: this.el.width / 2 - 35,
                y: this.el.height / 2 - 35
            };
            this.inMove = false;
            this.loaded = false;
        },
        events: {
            'click': 'move',
            'mousemove': 'mousemove',
            'keyup': 'keyPress',
            'keydown': 'keyPress',
            'keypress': 'keyPress'
        },
        render: function () {
            var that = this;
            resources.onReady(function () {
                that.loaded = true;
                that.image = resources.get('/images/ship.gif');
                that.ctx.translate(that.el.width / 2, that.el.height / 2);
                that.ctx.drawImage(that.image, -35, -35, 70, 70);
            });
            return this;
        },
        move: function (event) {
            this.model.setClickCoords({
                x: event.pageX,
                y: event.pageY
            });

            this.rotate(event);
            this.inMove = true;
        },
        moveDone: function () {
            this.inMove = false;
        },
        mousemove: function (event) {
            if (!(this.inMove) && this.loaded) {
                var ctx = this.ctx;
                ctx.clearRect(-35, -35, 70, 70);
                ctx.save();
                this.angle = Math.atan2(event.pageY - this.el.height / 2, event.pageX - this.el.width / 2) + Math.PI/2;
                // ctx.rotate(this.angle);
                ctx.drawImage(this.image, -35, -35, 70, 70);
                ctx.restore();
            }
        },
        rotate: function (event) {
            var ctx = this.ctx;
            ctx.clearRect(-35, -35, 70, 70);
            ctx.save();
            var newAngle = Math.atan2(event.pageY - this.el.height / 2, event.pageX - this.el.width / 2) + Math.PI/2;
            this.angle -= 2;
            // alert('rotate '+newAngle)
            ctx.rotate(newAngle);
            ctx.drawImage(this.image, -35, -35, 70, 70);
            ctx.restore();
        },
        keyPress: function(event) {
            console.log(event.keyCode);
            var pageX = 1,pageY = 1, disKoef = 100;
          
            if(event.keyCode == 87 || event.keyCode == 38) {
                pageY = shipModel.center.y - disKoef;
                pageX = shipModel.center.x - pageX;
            } else if(event.keyCode == 83 || event.keyCode == 40) {
                pageY = shipModel.center.y + disKoef;
                pageX = shipModel.center.x - pageX;
            } else if(event.keyCode == 68 || event.keyCode == 39) {
                pageY = shipModel.center.y + pageY;
                pageX = shipModel.center.x + disKoef;
            } else if(event.keyCode == 65 || event.keyCode == 37) {
                pageY = shipModel.center.y + pageY;
                pageX = shipModel.center.x - disKoef;
            }
            
            var dx = shipModel.center.x - pageX,
                dy = shipModel.center.y - pageY;

            if(shipModel._previousAttributes.x == dx && shipModel._previousAttributes.x != null) {
                dx += 1;
            } else if(shipModel._previousAttributes.y == dy && shipModel._previousAttributes.y != null) {
                dy += 1;
            }
            shipModel.set({type: 'move', x: dx, y: dy});
            shipModel.trigger('move:start', {x: dx, y: dy});
            var ev = new Object();
            ev.pageY = pageY;
            ev.pageX = pageX;
            shipModel.rotate(ev);
            shipModel.inMove = true;
            shipModel._previousAttributes.x = dx;
            shipModel._previousAttributes.y = dy;
        }
    });

    return new ShipView();

});