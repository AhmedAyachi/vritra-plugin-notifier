const proxy=require("cordova/exec/proxy");


module.exports={
    create:()=>{
        console.log("browser not supported");
    },
}

proxy.add("Notifications",module.exports);
