declare const Notifier:Notifier;

declare type Action={
    ref?:string,
    type:"button"|"input"|"select",
    label:string,
    options:string[],
    placeholder?:string,
    color?:string,
    icon?:string,
};

interface Notifier{
    notify(notification:{
        id?:number,
        title?:string,
        body?:string,
        icon?:string,
        largeIcon?:string,
        actions?:Action[],
        once?:boolean,
        onAction?(action:{
            ref:string,
            type:string,
            input:string,
        }):void,
    }):void;
    toast(props:{
        text:string,
        lasting:"short"|"long",
    }):void;
    destroy(notificationId:number):void;
}
