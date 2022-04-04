

class Notifier:NotifierPlugin,UNUserNotificationCenterDelegate{

    static let appname=Bundle.main.infoDictionary?["CFBundleDisplayName" as String] as? String ?? "";

    @objc(notify:)
    func notify(command:CDVInvokedUrlCommand){
        let props=command.arguments[0] as? [AnyHashable:Any];
        if !(props==nil){
            Notification.askPermissions({ granted,data in
            if(granted){
                //let settings=data as! UNNotificationSettings;
                let center=UNUserNotificationCenter.current();
                center.delegate=self;
                let notification=Notification(props!);
                center.add(notification.getRequest(),withCompletionHandler:{error in
                    print(error ?? "no error");
                });
            }
        });
        }
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,willPresent notification: UNNotification,withCompletionHandler completionHandler: (UNNotificationPresentationOptions)->Void){
        completionHandler([.alert,.badge,.sound]);
    }

    @objc(dismiss:)
    func dismiss(command:CDVInvokedUrlCommand){
        let argument=command.arguments[0] as? Int;
        if !(argument==nil){
            let id="\(Notifier.appname)_\(argument!)";
            let center=UNUserNotificationCenter.current();
            center.removePendingNotificationRequests(withIdentifiers:[id]);
            center.removeDeliveredNotifications(withIdentifiers:[id]);
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
