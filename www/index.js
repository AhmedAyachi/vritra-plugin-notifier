const exec=require("cordova/exec");


module.exports={
    notify:(notification)=>{
        const {onAction}=notification;
        exec(onAction,null,"Notifier","notify",[notification]);
    },
};
