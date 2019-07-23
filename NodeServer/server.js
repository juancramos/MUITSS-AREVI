const express = require("express");
const nodeStatic = require('node-static');
const https = require("https");
const socketIo = require("socket.io");
var fs = require("fs");
var options = {
    key: fs.readFileSync('key.pem'),
    cert: fs.readFileSync('cert.pem')
};

//Port from environment variable or default - 4001
const port = process.env.PORT || 4001;

//Setting up express and adding socketIo middleware
const app = express();

var fileServer = new (nodeStatic.Server)();
var server = https.createServer(options, function (req, res) {
    fileServer.serve(req, res);

});

const io = socketIo(server);

//Setting up a socket with the namespace "connection" for new sockets
io.on("connection", socket => {
    console.log(' %s sockets is connected', Object.keys(io.sockets.sockets).length);

    // //Here we listen on a new namespace called "incoming data"
    // socket.on("incoming data", (data) => {
    //     //Here we broadcast it out to all other sockets EXCLUDING the socket which sent us the data
    //     socket.broadcast.emit("outgoing data", { num: data });
    // });

    //A special namespace "disconnect" for when a client disconnects
    socket.on("disconnect", () => console.log("Client disconnected"));

    // convenience function to log server messages on the client
    function log() {
        var array = ['Message from server:'];
        array.push.apply(array, arguments);
        socket.emit('log', array);
    }

    socket.on('message', function (message) {
        log('Client said: ', message);
        // for a real app, would be room-only (not broadcast)
        socket.broadcast.emit('message', message);
    });

    socket.on('create or join', function (room) {
        log('Received request to create or join room ' + room);

        var clientsInRoom = io.sockets.adapter.rooms[room];
        var numClients = clientsInRoom ? Object.keys(clientsInRoom.sockets).length : 0;
        log('Room ' + room + ' now has ' + numClients + ' client(s)');

        if (numClients === 0) {
            socket.join(room);
            log('Client ID ' + socket.id + ' created room ' + room);
            socket.emit('created', room, socket.id);

        } else if (numClients === 1) {
            log('Client ID ' + socket.id + ' joined room ' + room);
            io.sockets.in(room).emit('join', room);
            socket.join(room);
            socket.emit('joined', room, socket.id);
            io.sockets.in(room).emit('ready');
        } else { // max two clients
            socket.emit('full', room);
        }
    });

    socket.on('ipaddr', function () {
        var ifaces = os.networkInterfaces();
        for (var dev in ifaces) {
            ifaces[dev].forEach(function (details) {
                if (details.family === 'IPv4' && details.address !== '127.0.0.1') {
                    socket.emit('ipaddr', details.address);
                }
            });
        }
    });

    socket.on('bye', function () {
        console.log('received bye');
    });
});

server.listen(port, () => console.log(`Listening on port ${port}`));
