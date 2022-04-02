

class Notifier:NotifierPlugin{
    
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
