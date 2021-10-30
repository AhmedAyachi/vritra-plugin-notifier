const exec=require("cordova/exec");


module.exports={
    notify:(notification)=>{
        const {onAction}=notification;
        exec(onAction,null,"Notifier","notify",[notification]);
    },
    showToast:(options)=>{
        exec(null,null,"Notifier","showToast",[options]);
    },
    cancelToast:(id,onCancelled)=>{
        exec(onCancelled,null,"Notifier","cancelToast",[id]);
    },
};
