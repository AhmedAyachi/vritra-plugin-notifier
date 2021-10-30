const exec=require("cordova/exec");


module.exports={
    notify:(notification)=>{
        const {onAction}=notification;
        exec(onAction,null,"Notifier","notify",[notification]);
    },
    toast:(options)=>{
        exec(null,null,"Notifier","toast",[options]);
    },
};
