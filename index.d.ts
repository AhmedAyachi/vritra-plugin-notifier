declare var Notifier:Notifier;

interface Notifier{
    create(options:{
        id:number,
        title:string,
        text:string,
        smallIcon:number,
        largeIcon:number,
        actions:[{
            label:string,
            icon:number,
        }],

    }):void,
}
