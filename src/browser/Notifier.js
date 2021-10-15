const proxy=require("cordova/exec/proxy");


module.exports={
    notify:()=>{
        console.log("browser not supported");
    },
}

proxy.add("Notifier",module.exports);
