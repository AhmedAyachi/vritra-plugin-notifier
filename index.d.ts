declare var Notifier:Notifier;

interface Notifier{
    notify(notification:{
        id:number,
        title:string,
        text:string,
        icon:string,
        largeIcon:number,
        actions:[{
            id:string|number,
            type:"button"|"input",
            label:string,
            icon:string,
        }],

    }):void,
}
