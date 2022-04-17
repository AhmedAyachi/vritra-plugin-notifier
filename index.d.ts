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
        fullbody?:boolean,//show all body text (respecting system characters limits)
        icon?:string,//android only: edit notification icon
        largeIcon?:"appIcon"|string,//base64 string or path to image file
        actions?:Action[],
        once?:boolean,//android only: dismiss notification on action: default true
        sticky?:boolean,//android only: prevent user from dismissing noti: default false
        subtext?:string,
        color?:string,//android only: notification main text color
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
