

class Notifier:NotifierPlugin,UNUserNotificationCenterDelegate{

    static var commands:[String:CDVInvokedUrlCommand]=[:];

    @objc(notify:)
    func notify(command:CDVInvokedUrlCommand){
        if let props=command.arguments[0] as? [AnyHashable:Any]{
            Notification.askPermissions({ granted,data in
                if(granted){
                    //let settings=data as! UNNotificationSettings;
                    let center=UNUserNotificationCenter.current();
                    center.delegate=self;
                    let notification=Notification(props);
                    center.add(notification.toRequest(),withCompletionHandler:{error in
                        if !(error==nil){
                            print(error!);
                        }
                    });
                    Notifier.commands[notification.id]=command;
                }
            });
        }
    }
    
    func userNotificationCenter(_ center:UNUserNotificationCenter,willPresent notification:UNNotification,withCompletionHandler completionHandler:@escaping(UNNotificationPresentationOptions)->Void){
        completionHandler([.alert,.badge,.sound]);
    }

    func userNotificationCenter(_ center:UNUserNotificationCenter,didReceive response:UNNotificationResponse,withCompletionHandler completionHandler:@escaping()->Void){
        let id:String=response.notification.request.identifier;
        print("id:",id);
        let actionId:String=response.actionIdentifier;
        if(actionId.contains("_")){
            let parts=actionId.split(separator:"_");
            let type=String(parts[0]);
            var data:[String:Any]=[
                "ref":parts[1],
                "type":type,
                "input":false,
            ];
            if let textResponse=response as? UNTextInputNotificationResponse {
                data["input"]=textResponse.userText;
            }
            else if(type.hasPrefix("select")){
                let option=parts.count>2 ? parts[2]:"";
                data["input"]=option;
            }
            if let command=Notifier.commands[id] {
                success(command,data);
                let reaction=id.contains("reaction");
                Notifier.commands.removeValue(forKey:id);
                if(reaction){
                    self.notify(command:command);
                }
            }
        }
        completionHandler();
    }

    @objc(dismiss:)
    func dismiss(command:CDVInvokedUrlCommand){
        let argument=command.arguments[0] as? Int;
        if !(argument==nil){
            let id="\(argument!)";
            let center=UNUserNotificationCenter.current();
            center.removePendingNotificationRequests(withIdentifiers:[id]);
            center.removeDeliveredNotifications(withIdentifiers:[id]);
        }
    }

    @objc(toast:)
    func toast(command:CDVInvokedUrlCommand){
        if let props=command.arguments[0] as? [AnyHashable:Any]{
            DispatchQueue.main.async(execute:{[self] in
                let text=props["text"] as? String;
                let lasting=(props["lasting"] as? String) ?? "short";
                let duration=lasting=="long" ? 3.5:2;
                let alert=UIAlertController(title:"",message:text,preferredStyle:.actionSheet);
                DispatchQueue.main.asyncAfter(deadline:DispatchTime.now()+duration){
                    alert.dismiss(animated:true);
                }
                self.viewController.present(alert,animated:true);
            });
        }
    }

}
