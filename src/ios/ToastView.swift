

class ToastView {

    let props:[String:Any];
    let controller:UIViewController;
    let style:[String:Any]?;
    var alert:UIAlertController?;
    var textview:UITextView?;
    var parent:UIView?;

    init(_ controller:UIViewController,_ props:[String:Any]){
        self.props=props;
        self.controller=controller;
        self.style=props["style"] as? [String:Any];
        let text=(props["text"] as? String) ?? "";
        if(style==nil){
            self.alert=UIAlertController(title:nil,message:text,preferredStyle:.actionSheet);
        }
        else{
            self.parent=controller.view!;
            self.textview=UITextView();
            if let textview=self.textview {
                let linecount=1+(text.count/40) as Int;
                textview.textAlignment = linecount==1 ? .center : .left;
                textview.text=text;
                textview.font=UIFont.systemFont(ofSize:UIScreen.main.bounds.width*16/393);
                textview.isEditable=false;
                textview.isScrollEnabled=false;
                textview.layer.masksToBounds=true;
                textview.alpha=0;
                setTextColor();
                setBackgroundColor();
                setCornerRadius();
                setBoundingRect(linecount);
                setPosition();
            }
        }
    }
    
    private func setTextColor(){
        let color=style?["color"] as? String ?? "black";
        textview!.textColor=getUIColorFromHex(color);
    }

    private func setBackgroundColor(){
        let backgroundColor=style?["backgroundColor"] as? String ?? "#ebebeb";
        textview!.backgroundColor=getUIColorFromHex(backgroundColor);
        
    }
    
    private func setCornerRadius(){
        let cornerRadius=style?["borderRadius"] as? Double ?? 60;
        textview!.layer.cornerRadius=cornerRadius*25/60;
    }

    private func setBoundingRect(_ linecount:Int){
        let textview=self.textview!;
        let parent=self.parent!;
        textview.sizeToFit();
        let padding=17.0;
        textview.frame.size.width=min(
            textview.frame.width+2*padding,
            parent.frame.width*0.9
        );
        textview.frame.size.height=(CGFloat(linecount)*textview.font!.lineHeight)+2*padding;
        textview.textContainerInset=UIEdgeInsets(
            top:padding,
            left:padding,
            bottom:padding,
            right:padding
        );
    }

    private func setPosition(){
        let textview=self.textview!;
        let parent=self.parent!;
        let align=style?["verticalAlign"] as? String ?? "bottom";
        let parentHeight=parent.frame.height;
        textview.center.x=parent.frame.width/2;
        if(align=="middle"){
            textview.center.y=parentHeight/2;
        }
        else{
            let offset=0.0855*parentHeight;
            textview.frame.origin.y=align=="top" ? offset : (parentHeight-textview.frame.height)-offset;
        }
    }
    
    func show(){
        let duration=getDuration();
        if(textview==nil){
            let alert=self.alert!;
            controller.present(alert,animated:true);
            DispatchQueue.main.asyncAfter(deadline:DispatchTime.now()+duration){
                alert.dismiss(animated:true);
            }
        }
        else{
            let textview=self.textview!;
            let parent=self.parent!;
            let fadeDuration=0.25;
            DispatchQueue.main.asyncAfter(deadline:DispatchTime.now()+duration){
                UIView.animate(
                    withDuration:fadeDuration,delay:0,
                    animations:{() in textview.alpha=0},
                    completion:{finsihed in
                        textview.removeFromSuperview();
                    }
                )
                
            }
            parent.addSubview(textview);
            textview.alpha=0;
            UIView.animate(
                withDuration:fadeDuration,delay:0,
                animations:{() in textview.alpha=self.getAlpha()}
            )
        }
    };
    
    private func getDuration()->Double{
        let lasting=(props["lasting"] as? String) ?? "short";
        return lasting=="long" ? 3.5 : 2.0;
    }
    
    private func getAlpha()->Double{
        let alpha=style?["opacity"] as? Double ?? 0.9;
        return alpha;
    }
}
