define([
    'backbone'
], function(Backbone) {
    var SocketsHandler = Backbone.Model.extend({
        isOpened: false,
        inited: false,
        badRequests: 0,
        create: function() {
            //ws://game.sronline.ru:8000/api/v1/game - для сервака
            //ws://127.0.0.1:8000/api/v1/game - для локалки
            var connection = new WebSocket('ws://127.0.0.1:8000/api/v1/game');
            this.connection = connection;
            var that = this;

            connection.onopen = function() {
                that.opened();
            };

            connection.onclose = function(event) {
                that.closed(event);
            }

            connection.onmessage = function(event) {
                that.newMessageReceived(event);
            }

            connection.onerror = function(error) {
                console.log('connection error', error)
            };

        },
        opened: function() {
            if (!this.inited) {
                this.trigger('connection:opened', this.connection); 
                this.badRequests = 0;
            }
        },
        closed: function(event) {
            if (!event.wasClean) {
                console.log('Closed with errors', event)
            }
            if (this.badRequests < 3) {
                this.create();
            }
            this.badRequests++
        },
        newMessageReceived: function(event) {
            this.trigger('connection:newMessage', event); 
            // GameView.messageReceived(event);
        }

    });

    return new SocketsHandler();
});