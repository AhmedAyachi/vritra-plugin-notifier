

class Notification {

    let content=UNMutableNotificationContent();
    let props:[AnyHashable:Any];
    static var categories:Set<UNNotificationCategory>=[];
    public var id:Int=0;

    init(_ props:[AnyHashable:Any]){
        self.props=props;
        content.title=props["title"] as? String ?? "";
        content.subtitle=props["subtext"] as? String ?? "";
        content.launchImageName="launchImageName";
        setBody();
        setAttachments();
        setActions();
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
        }
        content.attachments=attachments;
    }

    func setActions(){
        var id="\(Int.random(in:0...999))";
        var actions=props["actions"] as? [Any] ?? [];
        if !(actions.isEmpty){
            actions=Array<Any>(actions[..<(actions.count>3 ? 3:actions.count)]);
            var unactions:[UNNotificationAction]=[];
            actions.forEach({object in
                let action=object as! [AnyHashable:Any];
                let unaction=Notification.getUNAction(action);
                unactions.append(contentsOf:unaction);
                id=unaction[0].identifier+id;
            });
            let category=UNNotificationCategory(
                identifier:id,
                actions:unactions,
                intentIdentifiers:[],
                options:[]
            );
            Notification.categories.insert(category);
            UNUserNotificationCenter.current().setNotificationCategories(Notification.categories);
            content.categoryIdentifier=id;
        }
    }

    /* func setBadge(){
        let center=UNUserNotificationCenter.current();
        center.getDeliveredNotifications(completionHandler:{[self]unnotis in
            content.badge=unnotis.count+1 as NSNumber;
        });
        content.badge=1;
    } */
    
    func toRequest()->UNNotificationRequest{
        self.id=props["id"] as? Int ?? Int.random(in:0...99999);
        let request=UNNotificationRequest(
            identifier:"\(self.id)",
            content:content,
            trigger:nil
        );
        return request;
    }

    static func getUNAction(_ action:[AnyHashable:Any])->[UNNotificationAction]{
        let type=action["type"] as? String ?? "button";
        let ref="\(type)_\(action["ref"] as? String ?? "")";
        switch(type){
            case "input":
                let label=action["label"] as? String ?? "";
                return [UNTextInputNotificationAction(
                    identifier:ref,
                    title:label,
                    options:[],
                    textInputButtonTitle:label,
                    textInputPlaceholder:action["placeholder"] as? String ?? ""
                )];
            case "select":
                let options=action["options"] as? [String] ?? [];
                return options.map({option in UNNotificationAction(
                    identifier:"\(ref)_\(option)",
                    title:option,
                    options:[]
                )});
            case "button":fallthrough;
            default:return [UNNotificationAction(
                identifier:ref,
                title:action["label"] as? String ?? "",
                options:[]
            )];
        } 
    }

    static func getLargeIcon(_ icon:String)->UNNotificationAttachment{
        let id=Int.random(in:0...999);
        let base64=String(icon[icon.index(after:icon.firstIndex(of:",") ?? icon.index(before:icon.startIndex))...]);
        let data:Data=Data(base64Encoded:base64,options:.ignoreUnknownCharacters)!;
        let image:UIImage=UIImage(data:data)!;
        let attachment=UNNotificationAttachment.create("A\(id)",image)!;
        return attachment;
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
