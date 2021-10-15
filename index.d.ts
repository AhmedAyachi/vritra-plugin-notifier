declare var Notifier:Notifier;

interface Notifier{
    notify(notification:{
        id:number,
        title:string,
        text:string,
        smallIcon:number,
        largeIcon:number,
        actions:[{
            id:number,
            label:string,
            icon:number,
        }],

    }):void,
}
