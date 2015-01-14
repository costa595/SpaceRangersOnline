define([
    'jquery',
    'backbone',
    'resources',
    'models/user',
    'models/ship',
    'models/sun',
    'collections/otherShips',
], function($, Backbone, resources, userModel, shipModel, SunModel, OtherShipsCollection) {
    var ShipView = Backbone.View.extend({
        tagName: 'canvas',
        id: 'ship',
        className: 'ship',
        model: shipModel,
        initialize: function() {

            $(document).bind('keyup', _.bind(this.keyUp, this));
            $(document).bind('keydown', _.bind(this.keyDown, this));
            $(document).bind('keypress', _.bind(this.keyPress, this));
            
            // _.bindAll(this, 'handelMotion');
            // $(document).bind('deviceorientation', _.bind(this.handelMotion, this));
            // window.addEventListener('deviceorientation', this.handelMotion);

            $(window).bind("keydown", function(e) {
                if ([37, 38, 39, 40].indexOf(e.keyCode) > -1) {
                    e.preventDefault();
                }
            });

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
            this.angle = 0; //Угол, на который повернут
            this.inMove = false;
            this.loaded = false;

            this.movements = {
                top: false,
                left: false,
                right: false,
                bottom: false
            }
            this.disKoef = 100;

            this.selectedEnemyLogin = false;
            this.shot = {
                x: 0, //Текущие координаты
                y: 0,
                targetX: 0, //Конечная координата
                targetY: 0,
                xSpeed: 0, //Скорость
                ySpeed: 0,
                gunRadius: 0, //Радиус действия оружия. Если пуля вдруг дальше, то не рендерить ее
                interval: null, //Интеравал для анимации
                init: function(targetX, targetY, gunRadius) {
                    clearInterval(this.interval);
                    var shipHalthSize = 100 / 2; //Половина корабля
                    this.x = 0;
                    this.y = 0;
                    this.targetX = targetX + shipHalthSize;
this.targetY = targetY + shipHalthSize;
                    this.xSpeed = this.targetX / 3;
                    this.ySpeed = this.targetY / 3;
                    this.gunRadius = gunRadius;
                }
            }
        },
        events: {
            'click': 'clickHandler',
            'mousemove': 'mousemove',
            'mouseup' : 'mouseUpHandler',
        },
        render: function() {
            var that = this;
            this.ctx = this.el.getContext('2d');
            this.ctx.clearRect(-that.el.width, -that.el.height, this.el.width * 2, this.el.height * 2);
            resources.onReady(function() {
                if (!that.loaded) {
                    that.image = resources.get('/images/ship.gif');
                    that.ctx.translate(that.el.width / 2, that.el.height / 2);
                    that.ctx.drawImage(that.image, -35, -35, 70, 70);
                    that.loaded = true;
                }
            });
            this.ctx.save();
            this.ctx.rotate(this.angle);
            if (that.loaded) {
                this.ctx.drawImage(that.image, -35, -35, 70, 70);
            }
            // console.log('render this.selectedEnemyLogin', this.selectedEnemyLogin)
            if (this.selectedEnemyLogin != false) {
                var centerX = 0;
                var centerY = 0;
                for (var i = 1; i <= 5; i++) {
                    var weapon = userModel.get('w' + i);
                    if (weapon.id != 0) {
                        this.ctx.beginPath();
                        this.ctx.strokeStyle = "#00ff00";
                        this.ctx.arc(centerX, centerY, weapon.radius, 0, 2 * Math.PI, false);
                        this.ctx.stroke();
                    }
                }
            }
            this.ctx.restore();
            return this;
        },
        mouseUpHandler: function() {
            setTimeout(function() { 
                $('#cursor').css('visibility','hidden');
            }, 400);
        },
        clickHandler: function(event) {

            // $('.app').append('<div id="cursor"></div>');
            $('#cursor').css({'position':'absolute','visibility':'visible', 'z-index':'10000','width':'40px','height':'40px','background':'transparent url(/images/cursors/move_space_click.gif) 0 0 no-repeat'});
            var cursorOffset = {
                left : 22, 
                top  : 22
            }
            $('.app').on("click", function (e) {
                $('#cursor').offset({ 
                    left: (e.pageX - cursorOffset.left),
                    top : (e.pageY - cursorOffset.top)
                })

            });

            //Проверка на наличие объекта под кликом
            var modelCenter = this.model.getCenter();
            var isMoveEvent = true;
            var that = this;
            OtherShipsCollection.each(function(enemy) {
                var enemyPosition = enemy.getPosition();
                var dxClick = enemyPosition.x - (event.pageX - modelCenter.x) + 25;
                var dyClick = enemyPosition.y - (event.pageY - modelCenter.y) + 25;
                // var dx = enemyPosition.x - modelCenter.x - event.pageX;

                if (Math.abs(dxClick) < 25 && Math.abs(dyClick) < 25) {
                    enemy.markThisShip();
                    that.selectedEnemyLogin = enemy.get('login');
                    isMoveEvent = false;
                    that.render();
                }
            });

            if (isMoveEvent) {
                this.move(event);
            }
        },
        move: function(event) {
            this.model.setClickCoords({
                x: event.pageX,
                y: event.pageY
            });

            this.rotate(event);
            this.inMove = true;
        },
        moveDone: function() {
            this.inMove = false;
        },
        mousemove: function(event) {
            return;
            // if (!(this.inMove) && this.loaded) {
            //     var ctx = this.ctx;
            //     ctx.clearRect(-35, -35, 70, 70);
            //     ctx.save();
            //     this.angle = Math.atan2(event.pageY - this.el.height / 2, event.pageX - this.el.width / 2) + Math.PI / 2;
            //     ctx.rotate(this.angle);
            //     ctx.drawImage(this.image, -35, -35, 70, 70);
            //     ctx.restore();
            // }
        },
        rotate: function(event) {
            this.ctx.clearRect(-35, -35, 70, 70);
            this.ctx.save();
            var newAngle = Math.atan2(event.pageY - this.el.height / 2, event.pageX - this.el.width / 2) + Math.PI / 2;
            this.ctx.rotate(newAngle);
            this.angle = 0;
            // this.ctx.drawImage(this.image, -35, -35, 70, 70);
            this.render();
            this.angle = newAngle;
            this.ctx.restore();
        },
        keyUp: function(event) {
            if (event.keyCode == 87 || event.keyCode == 38) {
                this.movements.top = false;
            } else if (event.keyCode == 83 || event.keyCode == 40) {
                this.movements.bottom = false;
            } else if (event.keyCode == 68 || event.keyCode == 39) {
                this.movements.left = false;
            } else if (event.keyCode == 65 || event.keyCode == 37) {
                this.movements.right = false;
            }
        },
        //--------------------------------------для управление гироскопом (все работает, просто нам нахер это не нужно)
        // handelMotion: function(event) {
        //     var xAng = event.beta,
        //         yAng = event.gamma,
        //         pageX = 1,
        //         pageY = 1;

        //     var curPosX = shipModel.center.x,
        //         curPosY = shipModel.center.y;

        //     var handleEvent = false;

        //     if(yAng > 0) {
        //         this.movements.right = true;
        //         pageY = shipModel.center.y + pageY;
        //         pageX = shipModel.center.x + this.disKoef;
        //         handleEvent = true;
        //         console.log(pageX, pageY)
        //     }
        //     if (handleEvent) {
        //         var ev = new Object();
        //         ev.pageY = pageY;
        //         ev.pageX = pageX;
        //         this.move(ev);
        //         this.inMove = false;
        //     }

        // },
        keyDown: function(event) {

            var pageX = 1,
                pageY = 1;
            var handleEvent = false; //Обрабатыватьс обятие или нет? Без этого корабль летит в неикуда
            if (event.keyCode == 87 || event.keyCode == 38) {
                this.movements.top = true;
                pageY = this.model.center.y - this.disKoef;
                pageX = this.model.center.x - pageX;
                handleEvent = true;
            } else if (event.keyCode == 83 || event.keyCode == 40) {
                this.movements.bottom = true;
                pageY = shipModel.center.y + this.disKoef;
                pageX = shipModel.center.x - pageX;
                handleEvent = true;
            } else if (event.keyCode == 68 || event.keyCode == 39) {
                this.movements.left = true;
                pageY = shipModel.center.y + pageY;
                pageX = shipModel.center.x + this.disKoef;
                handleEvent = true;
            } else if (event.keyCode == 65 || event.keyCode == 37) {
                this.movements.right = true;
                pageY = shipModel.center.y + pageY;
                pageX = shipModel.center.x - this.disKoef;
                handleEvent = true;
            }
            if (handleEvent) {
                var ev = new Object();
                ev.pageY = pageY;
                ev.pageX = pageX;
                this.move(ev);
                this.inMove = false;
            }
        },
        keyPress: function(event) {
            //Стрельба
            if ([49, 50, 51, 52, 53].indexOf(event.keyCode) > -1) {
                if (this.selectedEnemyLogin != '') {
                    var gunId = event.keyCode - 48;
                    this.doFire(gunId);
                }

            }
        },
        doFire: function(gunId) { //Сделать выстрел, id - номре пушки
            var weapon = userModel.get('w' + gunId);
            var that = this;
            if (weapon.id != 0) {
                var curEnemy = OtherShipsCollection.where({
                    login: this.selectedEnemyLogin
                })
                enemy = curEnemy[0];
                var enemyPosition = enemy.getPosition();

                var distance = Math.sqrt(Math.pow(enemyPosition.x, 2) + Math.pow(enemyPosition.y, 2));
                if (distance < weapon.radius) {
                    //Анимая пули
                    this.shot.init(enemyPosition.x, enemyPosition.y, weapon.radius);
                    this.shot.interval = setInterval(function() {
                        that.animateShot();
                    }, 5);
                    //Передаем данные о выстреле
                    var connection = this.model.get('connection');
                    connection.send(JSON.stringify({
                        type: "fireToEnemy",
                        victimLogin: this.selectedEnemyLogin,
                        gunId: gunId,
                        sessionId: userModel.get("sessionId")
                    }));
                }
            }
        },
        enemyDied: function() {
            this.selectedEnemyLogin = false;
            this.render();
        },
        animateShot: function() {
            this.render();
            this.ctx.beginPath();
            this.ctx.arc(this.shot.x, this.shot.y, 3, 0, 2 * Math.PI, false);
            this.ctx.fillStyle = 'red';
            this.ctx.fill();
            //
            if (Math.abs(this.shot.xSpeed) >= Math.abs(this.shot.ySpeed)) {
                this.shot.targetX -= this.shot.xSpeed / Math.abs(this.shot.xSpeed) * 7;
                this.shot.targetY -= this.shot.ySpeed / Math.abs(this.shot.ySpeed) * Math.abs(this.shot.ySpeed / this.shot.xSpeed) * 7;
                this.shot.x += this.shot.xSpeed / Math.abs(this.shot.xSpeed) * 7;
                this.shot.y += this.shot.ySpeed / Math.abs(this.shot.ySpeed) * Math.abs(this.shot.ySpeed / this.shot.xSpeed) * 7;
            } else {
                this.shot.y += this.shot.ySpeed / Math.abs(this.shot.ySpeed) * 7;
                this.shot.targetY -= this.shot.ySpeed / Math.abs(this.shot.ySpeed) * 7;
                this.shot.x += this.shot.xSpeed / Math.abs(this.shot.xSpeed) * Math.abs(this.shot.xSpeed / this.shot.ySpeed) * 7;
                this.shot.targetX -= this.shot.xSpeed / Math.abs(this.shot.xSpeed) * Math.abs(this.shot.xSpeed / this.shot.ySpeed) * 7;
            }
            //
            var absX = Math.abs(this.shot.targetX);
            var absY = Math.abs(this.shot.targetY);
            // console.log('absX, absY', absX, absY, this.shot.x, this.shot.gunRadius)
            if ((absX < 2 || absX > this.shot.gunRadius) && (absY < 2 || absY > this.shot.gunRadius)) {
                clearInterval(this.shot.interval);
                this.render();
            }
        }
    });

    return new ShipView();

});





// define([
//     'jquery',
//     'backbone',
//     'resources',
//     'models/user',
//     'models/ship',
//     'models/sun',
//     'collections/otherShips',
// ], function($, Backbone, resources, userModel, shipModel, SunModel, OtherShipsCollection) {
//     var ShipView = Backbone.View.extend({
//         tagName: 'canvas',
//         id: 'ship',
//         className: 'ship',
//         model: shipModel,
//         initialize: function() {
//             _.bindAll(this, 'mouseDownHandler', 'mouseUpHandler')

//             $(document).bind('keyup', _.bind(this.keyUp, this));
//             $(document).bind('keydown', _.bind(this.keyDown, this));
//             $(document).bind('keypress', _.bind(this.keyPress, this));

//             $(window).bind("keydown", function(e) {
//                 if ([37, 38, 39, 40].indexOf(e.keyCode) > -1) {
//                     e.preventDefault();
//                 }
//             });

//             this.ctx = this.el.getContext('2d');
//             resources.load(['/images/ship.gif']);
//             this.el.width = $('body').width();
//             this.el.height = $('body').height();
//             this.model.setCenter({
//                 x: this.el.width / 2,
//                 y: this.el.height / 2
//             });
//             this.shipPos = {
//                 x: this.el.width / 2 - 35,
//                 y: this.el.height / 2 - 35
//             };
//             this.angle = 0; //Угол, на который повернут
//             this.inMove = false;
//             this.loaded = false;

//             this.movements = {
//                 top: false,
//                 left: false,
//                 right: false,
//                 bottom: false
//             }
//             this.disKoef = 100;

//             this.selectedEnemyLogin = false;
//             this.shot = {
//                 x: 0, //Текущие координаты
//                 y: 0,
//                 targetX: 0, //Конечная координата
//                 targetY: 0,
//                 xSpeed: 0, //Скорость
//                 ySpeed: 0,
//                 gunRadius: 0, //Радиус действия оружия. Если пуля вдруг дальше, то не рендерить ее
//                 interval: null, //Интеравал для анимации
//                 init: function(targetX, targetY, gunRadius) {
//                     clearInterval(this.interval);
//                     var shipHalthSize = 100 / 2; //Половина корабля
//                     this.x = 0;
//                     this.y = 0;
//                     this.targetX = targetX + shipHalthSize;
//                     this.targetY = targetY + shipHalthSize;
//                     this.xSpeed = this.targetX / 3;
//                     this.ySpeed = this.targetY / 3;
//                     this.gunRadius = gunRadius;
//                 }
//             }
//         },
//         events: {
//             'click': 'clickHandler',
//             'mousemove': 'mousemove',
//             'mousedown' : 'mouseDownHandler',
//             'mouseup' : 'mouseUpHandler'
//         },
//         render: function() {
//             var that = this;
//             this.ctx = this.el.getContext('2d');
//             this.ctx.clearRect(-that.el.width, -that.el.height, this.el.width * 2, this.el.height * 2);
//             resources.onReady(function() {
//                 if (!that.loaded) {
//                     that.image = resources.get('/images/ship.gif');
//                     that.ctx.translate(that.el.width / 2, that.el.height / 2);
//                     that.ctx.drawImage(that.image, -35, -35, 70, 70);
//                     that.loaded = true;
//                 }
//             });
//             this.ctx.save();
//             this.ctx.rotate(this.angle);
//             if (that.loaded) {
//                 this.ctx.drawImage(that.image, -35, -35, 70, 70);
//             }

//             if (this.selectedEnemyLogin != false) {
//                 var centerX = 0;
//                 var centerY = 0;
//                 for (var i = 1; i <= 5; i++) {
//                     var weapon = userModel.get('w' + i);
//                     if (weapon.id != 0) {
//                         this.ctx.beginPath();
//                         this.ctx.strokeStyle = "#00ff00";
//                         this.ctx.arc(centerX, centerY, weapon.radius, 0, 2 * Math.PI, false);
//                         this.ctx.stroke();
//                     }
//                 }
//             }
//             this.ctx.restore();

//             this.$comeCursor = $('<div />');
//             this.$comeCursor.css({
//                 'position':'absolute', 
//                 'zIndex':'10000',
//                 'width':'40px',
//                 'height':'40px',
//                 'background':'transparent url(/images/cursors/move_space_click.gif) 0 0 no-repeat',
//                 visibility: 'hidden'
//             });

//             $('#game').append(this.$comeCursor);
//             console.log(this.$comeCursor);

//             return this;
//         },
//         mouseDownHandler: function(e) {

//                 var cursorOffset = {
//                    left : 22, 
//                    top  : 22
//                 }
//                 this.$comeCursor.css({ 
//                     left: (e.pageX - cursorOffset.left) + 'px',
//                     top : (e.pageY - cursorOffset.top) + 'px',
//                     visibility: 'visible'
//                 });
            
//         },
//         mouseUpHandler: function() {
//             var _that = this;
//             setTimeout(function() { 
//                 _that.$comeCursor.css({ 
//                     visibility: 'hidden'
//                 });
//             }, 1000);
//         },
//         clickHandler: function(event) {
//             //Проверка на наличие объекта под кликом
//             var modelCenter = this.model.getCenter();
//             var isMoveEvent = true;
//             var that = this;
//             OtherShipsCollection.each(function(enemy) {
//                 var enemyPosition = enemy.getPosition();
//                 var dxClick = enemyPosition.x - (event.pageX - modelCenter.x) + 25;
//                 var dyClick = enemyPosition.y - (event.pageY - modelCenter.y) + 25;
//                 // var dx = enemyPosition.x - modelCenter.x - event.pageX;

//                 if (Math.abs(dxClick) < 25 && Math.abs(dyClick) < 25) {
//                     enemy.markThisShip();
//                     that.selectedEnemyLogin = enemy.get('login');
//                     isMoveEvent = false;
//                     that.render();
//                 }
//             });

//             if (isMoveEvent) {
//                 this.move(event);
//             }
//         },
//         move: function(event) {
//             this.model.setClickCoords({
//                 x: event.pageX,
//                 y: event.pageY
//             });

//             this.rotate(event);
//             this.inMove = true;
//         },
//         moveDone: function() {
//             this.inMove = false;
//         },
//         mousemove: function(event) {
//             return;
//             // if (!(this.inMove) && this.loaded) {
//             //     var ctx = this.ctx;
//             //     ctx.clearRect(-35, -35, 70, 70);
//             //     ctx.save();
//             //     this.angle = Math.atan2(event.pageY - this.el.height / 2, event.pageX - this.el.width / 2) + Math.PI / 2;
//             //     ctx.rotate(this.angle);
//             //     ctx.drawImage(this.image, -35, -35, 70, 70);
//             //     ctx.restore();
//             // }
//         },
//         rotate: function(event) {
//             this.ctx.clearRect(-35, -35, 70, 70);
//             this.ctx.save();
//             var newAngle = Math.atan2(event.pageY - this.el.height / 2, event.pageX - this.el.width / 2) + Math.PI / 2;
//             this.ctx.rotate(newAngle);
//             this.angle = 0;
//             // this.ctx.drawImage(this.image, -35, -35, 70, 70);
//             this.render();
//             this.angle = newAngle;
//             this.ctx.restore();
//         },
//         keyUp: function(event) {
//             if (event.keyCode == 87 || event.keyCode == 38) {
//                 this.movements.top = false;
//             } else if (event.keyCode == 83 || event.keyCode == 40) {
//                 this.movements.bottom = false;
//             } else if (event.keyCode == 68 || event.keyCode == 39) {
//                 this.movements.left = false;
//             } else if (event.keyCode == 65 || event.keyCode == 37) {
//                 this.movements.right = false;
//             }
//         },
//         keyDown: function(event) {

//             var pageX = 1,
//                 pageY = 1;
//             var handleEvent = false; //Обрабатыватьс обятие или нет? Без этого корабль летит в неикуда
//             if (event.keyCode == 87 || event.keyCode == 38) {
//                 this.movements.top = true;
//                 pageY = this.model.center.y - this.disKoef;
//                 pageX = this.model.center.x - pageX;
//                 handleEvent = true;
//             } else if (event.keyCode == 83 || event.keyCode == 40) {
//                 this.movements.bottom = true;
//                 pageY = shipModel.center.y + this.disKoef;
//                 pageX = shipModel.center.x - pageX;
//                 handleEvent = true;
//             } else if (event.keyCode == 68 || event.keyCode == 39) {
//                 this.movements.left = true;
//                 pageY = shipModel.center.y + pageY;
//                 pageX = shipModel.center.x + this.disKoef;
//                 handleEvent = true;
//             } else if (event.keyCode == 65 || event.keyCode == 37) {
//                 this.movements.right = true;
//                 pageY = shipModel.center.y + pageY;
//                 pageX = shipModel.center.x - this.disKoef;
//                 handleEvent = true;
//             }
//             if (handleEvent) {
//                 var ev = new Object();
//                 ev.pageY = pageY;
//                 ev.pageX = pageX;
//                 this.move(ev);
//                 this.inMove = false;
//             }
//         },
//         keyPress: function(event) {
//             //Стрельба

//             if ([49, 50, 51, 52, 53].indexOf(event.keyCode) > -1) {
//                 if (this.selectedEnemyLogin != '') {
//                     var gunId = event.keyCode - 48;
//                     this.doFire(gunId);
//                 }

//             }
//         },
//         doFire: function(gunId) { //Сделать выстрел, id - номре пушки
//             var weapon = userModel.get('w' + gunId);
//             var that = this;
//             if (weapon.id != 0) {
//                 var curEnemy = OtherShipsCollection.where({
//                     login: this.selectedEnemyLogin
//                 })
//                 enemy = curEnemy[0];
//                 var enemyPosition = enemy.getPosition();

//                 var distance = Math.sqrt(Math.pow(enemyPosition.x, 2) + Math.pow(enemyPosition.y, 2));
//                 if (distance < weapon.radius) {
//                     //Анимая пули
//                     this.shot.init(enemyPosition.x, enemyPosition.y, weapon.radius);
//                     this.shot.interval = setInterval(function() {
//                         that.animateShot();
//                     }, 5);
//                     //Передаем данные о выстреле
//                     var connection = this.model.get('connection');
//                     connection.send(JSON.stringify({
//                         type: "fireToEnemy",
//                         victimLogin: this.selectedEnemyLogin,
//                         gunId: gunId,
//                         sessionId: userModel.get("sessionId")
//                     }));
//                 }
//             }
//         },
//         enemyDied: function() {
//             this.selectedEnemyLogin = false;
//             this.render();
//         },
//         animateShot: function() {
//             this.render();
//             this.ctx.beginPath();
//             this.ctx.arc(this.shot.x, this.shot.y, 3, 0, 2 * Math.PI, false);
//             this.ctx.fillStyle = 'red';
//             this.ctx.fill();
//             //
//             if (Math.abs(this.shot.xSpeed) >= Math.abs(this.shot.ySpeed)) {
//                 this.shot.targetX -= this.shot.xSpeed / Math.abs(this.shot.xSpeed) * 7;
//                 this.shot.targetY -= this.shot.ySpeed / Math.abs(this.shot.ySpeed) * Math.abs(this.shot.ySpeed / this.shot.xSpeed) * 7;
//                 this.shot.x += this.shot.xSpeed / Math.abs(this.shot.xSpeed) * 7;
//                 this.shot.y += this.shot.ySpeed / Math.abs(this.shot.ySpeed) * Math.abs(this.shot.ySpeed / this.shot.xSpeed) * 7;
//             } else {
//                 this.shot.y += this.shot.ySpeed / Math.abs(this.shot.ySpeed) * 7;
//                 this.shot.targetY -= this.shot.ySpeed / Math.abs(this.shot.ySpeed) * 7;
//                 this.shot.x += this.shot.xSpeed / Math.abs(this.shot.xSpeed) * Math.abs(this.shot.xSpeed / this.shot.ySpeed) * 7;
//                 this.shot.targetX -= this.shot.xSpeed / Math.abs(this.shot.xSpeed) * Math.abs(this.shot.xSpeed / this.shot.ySpeed) * 7;
//             }
//             //
//             var absX = Math.abs(this.shot.targetX);
//             var absY = Math.abs(this.shot.targetY);
//             // console.log('absX, absY', absX, absY, this.shot.x, this.shot.gunRadius)
//             if ((absX < 2 || absX > this.shot.gunRadius) && (absY < 2 || absY > this.shot.gunRadius)) {
//                 clearInterval(this.shot.interval);
//                 this.render();
//             }
//         }
//     });

//     return new ShipView();

// });
