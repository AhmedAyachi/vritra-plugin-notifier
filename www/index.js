const exec=require("cordova/exec");


module.exports={
    create:(options)=>{
        const {onCreated,onTap}=options;
        exec(onCreated,onTap,"Notifier","create",[options]);
    },
};
