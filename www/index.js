const exec=require("cordova/exec");


module.exports={
    notify:(notification)=>{
        const {onCreated,onTap}=notification;
        exec(onCreated,onTap,"Notifier","notify",[notification]);
    },
};
