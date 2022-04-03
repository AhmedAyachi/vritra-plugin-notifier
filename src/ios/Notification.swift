

class Notification {

    public let request:UNNotificationRequest;

    init(_ props:[AnyHashable:Any]){
        let content=UNMutableNotificationContent();
        content.title=props["title"] as? String ?? "title";
        content.body=props["body"] as? String ?? "body";
        let id=props["id"] as? Int ?? Int.random(in:0...999);
        self.request=UNNotificationRequest(
            identifier:String(id),
            content:content,
            trigger:nil
        );
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
