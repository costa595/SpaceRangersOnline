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
            var coords = this.get('coords');
            var sunCoords = SunModel.getPosition();
            
            if (coords && sunCoords) {
                var xDistance = coords.x - sunCoords.x;
                var yDistance = coords.y - sunCoords.y;
                console.log('sendData coords', coords.x, coords.y, ' -- ', sunCoords.x, sunCoords.y, ' -- ', xDistance, yDistance);
                // TODO: Передавать текущие координаты, а не только то, куда полететь хочет
                if (coords.x != undefined && coords.y != undefined) {
                    var request = {
                        type: 'updateCoords',
                        x: xDistance,
                        y: yDistance,
                        sessionId: userModel.get("sessionId")
                    }
                    this.get('connection').send(JSON.stringify(request));
                }
            }
        },
        setCenter: function (coords) {
            this.center = coords;
        },
        setClickCoords: function (coords) {
            var dx = this.center.x - coords.x,
                dy = this.center.y - coords.y;

            if(this._previousAttributes.x == dx && this._previousAttributes.x != null) {
                dx += 1;
            }

            this.set({type: 'move', x: dx, y: dy, coords: coords});
            SunModel.startMove({x: dx, y: dy});
            this.trigger('move:start', {x: dx, y: dy}); 
            this._previousAttributes.x = dx;

            console.log('setClickCoords ', coords)
            //Объект солнца почему-то не может слушать траггер, заменил на вызов метода
            
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