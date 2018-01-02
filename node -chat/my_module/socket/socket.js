
exports.connectSocket = (app,server)=>{
    var user = require("../database/user");
    user.create(); 
    var chat = require("../database/chat");
    chat.create();

    var io = require("socket.io").listen(server);

    //array save all name user
    var mang_user = [];

    io.sockets.on("connection",function(socket){
        console.log("connection sockket");
        //send user online
        socket.emit("send_all_user",{users:mang_user});
        
        //register
        socket.on("register_user",data=>{
           
            user.find({nickName:data}).then(function(item){
                if (item.length == 0) {
                    socket.emit("register_res",{result:1});
                    user.addCollection({nickName:data});
                }else{
                    socket.emit("register_res",{result:0}); 
                }
            });

        });
        
        //login
        socket.on("login",data=>{
            user.find({nickName:data}).then(function(item){
                if (item.length != 0) {
                    socket.emit("login_res",{result:1});
                    
                    mang_user.push(data);
                    io.emit("user_new",{user_new:data});
                }else{
                    socket.emit("login_res",{result:0}); 
                }
            });

        });
        
        // msg
        socket.on("new_msg",data=>{
    
        });
    });
}