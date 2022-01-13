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
        fullbody?:boolean,
        icon?:string,
        largeIcon?:"appIcon"|string,
        actions?:Action[],
        once?:boolean,
        sticky?:boolean,
        subtext?:string,
        color?:string,
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
    dismiss(notificationId:number):void;
}
