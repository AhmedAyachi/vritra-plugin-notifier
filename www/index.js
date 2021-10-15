const exec=require("cordova/exec");


module.exports={
    notify:(options)=>{
        const {onCreated,onTap}=options;
        exec(onCreated,onTap,"Notifier","notify",[options]);
    },
};
