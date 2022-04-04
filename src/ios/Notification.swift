

class Notification {

    let content=UNMutableNotificationContent();
    let props:[AnyHashable:Any];

    init(_ props:[AnyHashable:Any]){
        self.props=props;
        content.title=props["title"] as? String ?? "";
        content.subtitle=props["subtext"] as? String ?? "";
        setBody();
    }
    
    func getRequest()->UNNotificationRequest{
        let id=props["id"] as? Int ?? Int.random(in:0...999);
        let request=UNNotificationRequest(
            identifier:"\(Notifier.appname)_\(id)",
            content:content,
            trigger:nil
        );
        return request;
    }

    func setBody(){
        var body=props["body"] as? String ?? " ";
        let fullbody=props["fullbody"] as? Bool ?? false;
        body=fullbody ? body:String(body[...body.index(body.startIndex,offsetBy:body.count<89 ? body.count-1:89)]);
        content.body=body;
    }

    static func askPermissions(_ onGranted:@escaping(Bool,Any)->Void){
        let center=UNUserNotificationCenter.current();
        center.requestAuthorization(options:[.alert,.sound,.badge],completionHandler:{granted,error  in
            if(granted){
                center.getNotificationSettings(completionHandler:{ settings in
                    onGranted(granted,settings);
                });
            }
            else{
                onGranted(granted,error ?? false);
            }
        });
    }
}
