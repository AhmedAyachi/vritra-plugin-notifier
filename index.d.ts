declare var Notifier:Notifier;

declare type Action={
    value:string|number,
    type:"button"|"input"|"select",
    options:string[],
    placeholder:string,
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
        onAction(action:{
            value:string,
            type:string,
            input:string,
        }):void,
    }):void,
    showToast(toast:{
        id:string,
        text:string,
        lasting:"short"|"long",
    }):void,
    cancelToast(id:string,onCancelled:()=>void):void,
}
