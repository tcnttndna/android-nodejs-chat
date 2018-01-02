var express = require("express");
var app = express();
var server = require("http").createServer(app);
var socket = require("./my_module/socket/socket");
socket.connectSocket(app,server);
server.listen(process.env.Port || 3000,()=> console.log("connection port 3000"));


