

class Notifier:NotifierPlugin{

    @objc(notify:)
    func notify(command:CDVInvokedUrlCommand){
        let props=command.arguments[0] as? [AnyHashable:Any];
        if !(props==nil){
            Notification.askPermissions({ granted,data in
            if(granted){
                //let settings=data as! UNNotificationSettings;
                let center=UNUserNotificationCenter.current();
                let notification=Notification(props!);
                center.add(notification.request,withCompletionHandler:{error in
                    print(error ?? "no error");
                });
            }
        });
        }
    }

    @objc(toast:)
    func toast(command:CDVInvokedUrlCommand){
        let argument=command.arguments[0] as? [AnyHashable:Any];
        if !(argument==nil){
            let props=argument!;
            let text=props["text"] as? String;
            let lasting=(props["lasting"] as? String) ?? "short";
            let duration=lasting=="long" ? 3.5:2;
            let alert=UIAlertController(title:"",message:text,preferredStyle:.actionSheet);
            DispatchQueue.main.asyncAfter(deadline:DispatchTime.now()+duration){
                alert.dismiss(animated:true);
            }
            self.viewController.present(alert,animated:true);
        }
    }

}
