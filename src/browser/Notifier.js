const proxy=require("cordova/exec/proxy");


module.exports={
    notify:()=>{
        console.log("browser not supported");
    },
    toast:({text})=>{
        alert(text);
    },
}

proxy.add("Notifier",module.exports);
