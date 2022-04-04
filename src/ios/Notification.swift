

class Notification {

    let content=UNMutableNotificationContent();
    let props:[AnyHashable:Any];

    init(_ props:[AnyHashable:Any]){
        self.props=props;
        content.title=props["title"] as? String ?? "";
        content.subtitle=props["subtext"] as? String ?? "";
        setBody();
        setAttachments();
    }

    func setBody(){
        var body=props["body"] as? String ?? " ";
        let fullbody=props["fullbody"] as? Bool ?? false;
        body=fullbody ? body:String(body[...body.index(body.startIndex,offsetBy:body.count<89 ? body.count-1:89)]);
        content.body=body;
    }

    func setAttachments(){
        var attachments:[UNNotificationAttachment]=[];
        let largeIcon=props["largeIcon"] as?String;
        if !(largeIcon==nil){
            attachments.append(Notification.getLargeIcon(largeIcon!));
            attachments.append(Notification.getLargeIcon(largeIcon!));
        }
        content.attachments=attachments;
    }

    static func getLargeIcon(_ icon:String)->UNNotificationAttachment{
        let id=Int.random(in:0...999);
        let base64=String(icon[icon.index(after:icon.firstIndex(of:",") ?? icon.index(before:icon.startIndex))...]);
        let data:Data=Data(base64Encoded:base64,options:.ignoreUnknownCharacters)!;
        let image:UIImage=UIImage(data:data)!;
        let attachment=UNNotificationAttachment.create("A\(id)",image)!;
        return attachment;
    }

    /* func setBadge(){
        let center=UNUserNotificationCenter.current();
        center.getDeliveredNotifications(completionHandler:{[self]unnotis in
            content.badge=unnotis.count+1 as NSNumber;
        });
        content.badge=1;
    } */
    
    func toRequest()->UNNotificationRequest{
        let id=props["id"] as? Int ?? Int.random(in:0...999);
        let request=UNNotificationRequest(
            identifier:"\(Notifier.appname)_\(id)",
            content:content,
            trigger:nil
        );
        return request;
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

extension UNNotificationAttachment {
    static func create(_ identifier:String,_ image:UIImage,_ options:[NSObject:AnyObject]?=nil)->UNNotificationAttachment?{
        let fileManager=FileManager.default;
        let tmpSubFolderName=ProcessInfo.processInfo.globallyUniqueString;
        let tmpSubFolderURL=URL(fileURLWithPath:NSTemporaryDirectory()).appendingPathComponent(tmpSubFolderName,isDirectory:true);
        do{
            try fileManager.createDirectory(at:tmpSubFolderURL,withIntermediateDirectories:true,attributes:nil);
            let imageFileIdentifier="largeIcon.jpeg";
            let fileURL=tmpSubFolderURL.appendingPathComponent(imageFileIdentifier);
            let imageData=UIImage.pngData(image);
            try imageData()?.write(to:fileURL);
            let imageAttachment=try UNNotificationAttachment.init(identifier:identifier,url:fileURL,options:options);
            return imageAttachment;
        } 
        catch{
            print("error \(error.localizedDescription)");
        }
        return nil;
    }
}
