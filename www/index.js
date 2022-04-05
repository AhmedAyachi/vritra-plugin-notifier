const exec=require("cordova/exec");


module.exports={
    notify:(notification)=>{
        const {onAction}=notification;
        exec(onAction,null,"Notifier","notify",[notification]);
    },
    toast:(options)=>{
        options.text=`${options.text}`;
        exec(null,null,"Notifier","toast",[options]);
    },
    dismiss:(notificationId)=>{
        exec(null,null,"Notifier","dismiss",[notificationId]);
    },
};
