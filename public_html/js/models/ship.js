define([
    'backbone',
    'models/user',
    'models/sun'
], function (Backbone, userModel, SunModel) {
    var ShipModel = Backbone.Model.extend({
        initialize: function () {
            this.on('change', this.sendData, this);
            this.coords = new Object();
        },
        sendData: function () {
            var coords = this.get('coords'); //
            var sunCoords = SunModel.getPosition();
            
            if (coords && sunCoords) {
                var xDistance = coords.x - sunCoords.x;
                var yDistance = coords.y - sunCoords.y;
                var curCoordsX = xDistance + this.get('x');
                var curCoordsY = yDistance + this.get('y');
                // console.log('sendData coords', coords.x, coords.y, ' -- ', sunCoords.x, sunCoords.y, ' -- ', xDistance, yDistance, ' -- ', this.get('x'), this.get('y'), ' -- ', curCoordsX, curCoordsY);
                // TODO: Передавать текущие координаты, а не только то, куда полететь хочет
                if (coords.x != undefined && coords.y != undefined) {
                    var request = {
                        type: 'updateCoords',
                        x: xDistance,
                        y: yDistance,
                        curX: curCoordsX,
                        curY: curCoordsY,
                        sessionId: userModel.get("sessionId")
                    }
                    try {
                    this.get('connection').send(JSON.stringify(request));
                } catch(e) {}
                }
            }
        },
        setCenter: function (coords) {
            this.center = coords;
        },
        getCenter: function () {
            return this.center;
        },
        setClickCoords: function (coords) {
            var dx = this.center.x - coords.x,
                dy = this.center.y - coords.y;

            if(this._previousAttributes.x == dx && this._previousAttributes.x != null) {
                dx += 1;
            } else if(this._previousAttributes.y == dy && this._previousAttributes.y != null) {
                dy += 1;
            }

            this.set({type: 'move', x: dx, y: dy, coords: coords});
            SunModel.startMove({x: dx, y: dy});
            this.trigger('move:start', {x: dx, y: dy}); 
            this._previousAttributes.y = dy;
            this._previousAttributes.x = dx;

            
        },
        setSunPos: function (centerPos) {
            this.sunPos.x = centerPos.x + 55; // TODO: допилить на рандом
            this.sunPos.y = centerPos.y + 55;
        },
        getSunPos: function () {
            return this.sunPos;
        }
    });

    return new ShipModel();
});