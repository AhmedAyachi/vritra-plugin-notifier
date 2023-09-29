declare const Notifier:Notifier;


interface Notifier{
    notify(notification:{
        /**
        * Notification id.
        * Used to dismiss the notification 
        */
        id?:number,
        title?:string,
        subtext?:string,
        body?:string,
        /**
        * show all body text (respecting system characters limits).
        * Default: false
        */
        fullbody?:boolean,
        /**
        * Edits notification icon
        * Supports a path or a base64 string
        * android only
        */
        icon?:string,
        /**
        * An icon that is shown on the middle right side of the notification. 
        * Used to Attach an image to the notification. 
        * Supports a base64 string or path to image file.
        * The value "appIcon" will use the app icon.
        */
        largeIcon?:"appIcon"|string,
        /**
        * Notification actions.
        * Allows up to 3 actions. 
        */
        actions?:Action[],
        /**
        * if true, Dismiss notification on user interaction.
        * Default: true
        */
        once?:boolean,
        /**
        * Prevents the user from dismissing the notification. 
        * Android only.
        * Default: false. 
        */
        sticky?:boolean,
        /**
        * Notification main text color.
        * Supports an hex color code.
        * Android only.
        */
        color?:string,
        onAction?(action:{
            ref:string,
            type:string,
            /**
            * The user input for actions with type "input". 
            */
            input:string,
        }):void,
    }):void;
    /**
     * 
     * @param props Toast message props
     * @android custom toasts deprecated since android 11 (API 30).
     */
    toast(props:{
        text:string,
        /**
        * Specifies how long the toast will last before disappearing.
        */
        lasting:"short"|"long",
        /**
         * @default "bottom"
         */
        verticalAlign:"top"|"middle"|"bottom",
        /**
         * @default "black"
         */
        color:ToastColor,
        /**
         * @default "white"
         */
        backgroundColor:ToastColor,
    }):void;
    /**
    * Dismissed notification with the specified id.
    */
    dismiss(notificationId:number):void;
}

type Action={
    /**
    * Used to identify the action 
    */
    ref?:string,
    /**
    * Default: "button"
    */
    type?:"button"|"input"|"select",
    label:string,
    /**
    * The options of a select action 
    */
    options:string[],
    /**
    * The placeholder of an input action 
    */
    placeholder?:string,
    /**
    * The label color.
    * Android only
    */
    color?:string,
    /**
    * The action icon.
    * Supports a path or a base64 string.
    * Android <7 only. 
    */
    icon?:string,
};

type ToastColor=(
    "black"|"blue"|"brown"|"cyan"|
    "darkgray"|"gray"|"green"|"lightgray"|
    "magenta"|"orange"|"purple"|"red"|
    "yellow"|"white"|"transparent"|"#"
)
