declare var Notifier:Notifier;

interface Notifier{
    notify(notification:{
        id:number,
        title:string,
        text:string,
        icon:string,
        largeIcon:string,
        actions:[{
            id:number,
            label:string,
            icon:number,
        }],

    }):void,
}
