declare var Notifier:Notifier;

interface Notifier{
    notify(notification:{
        id:number,
        title:string,
        text:string,
        smallIcon:number,
        largeIcon:number,
        actions:[{
            id:string|number,
            type:"button"|"input",
            label:string,
            icon:string,
        }],

    }):void,
}
