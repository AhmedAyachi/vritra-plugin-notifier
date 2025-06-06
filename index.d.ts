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
        actions?:Action<keyof ActionOptions>[],
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
        * @default "short"
        */
        lasting?:"short"|"long",
        /**
         * @notice If provided, a view is used instead of a native toast
         * because native toasts are not customizable
         */
        style?:{
            /**
             * @default "bottom"
             */
            verticalAlign?:"top"|"middle"|"bottom",
            /**
             * The toast text color
             * @default "black"
             */
            color?:VritraColor,
            /**
             * @default "#ebebeb"
             */
            backgroundColor?:VritraColor,
            /**
             * @default 0.9
             */
            opacity?:number,
            /**
             * @default 60
             */
            borderRadius?:number,
        },
        /**
        * @default "bottom"
        * @deprecated use the style object instead
        */
        verticalAlign?:"top"|"middle"|"bottom",
        /**
         * The toast text color
         * @default "black"
         * @deprecated use the style object instead
         */
        color?:VritraColor,
        /**
         * @default "white"
         * @deprecated use the style object instead
         */
        backgroundColor?:VritraColor,
    }):void;
    /**
    * Dismissed notification with the specified id.
    */
    dismiss(notificationId:number):void;
}

type Action<Type extends keyof ActionOptions>={
    /**
    * Used to identify the action 
    */
    ref?:string,
    /**
    * Default: "button"
    */
    type?:Type,
    label:string,
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
}&ActionOptions[Type];

type ActionOptions={
    "input":{
        placeholder?:string,
    },
    "button":{},
    "select":{
        options:string[],
        placeholder?:string,
    },
}

type VritraColor=(
    "black"|"blue"|"brown"|"cyan"|
    "darkgray"|"gray"|"green"|"lightgray"|
    "magenta"|"orange"|"purple"|"red"|
    "yellow"|"white"|"transparent"|"#"
)
