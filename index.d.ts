declare var Notifier:Notifier;

declare type Action={
    id:string|number,
    type:"button"|"input",
    label:string,
    color:string,
    icon:string,
};

interface Notifier{
    notify(notification:{
        id:string,
        title:string,
        text:string,
        icon:string,
        largeIcon:string,
        backgroundColor:string,
        actions:Action[],
        onAction(actionId:string):void,
    }):void,
}
