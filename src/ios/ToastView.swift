

class ToastView {

    let textview=UITextView();
    let props:[String:Any];
    let parent:UIView;

    init(_ parent:UIView,_ props:[String:Any]){
        self.parent=parent;
        self.props=props;
        let text=(props["text"] as? String) ?? "";
        let linecount=1+(text.count/40) as Int;
        textview.textAlignment = linecount==1 ? .center : .left;
        textview.text=text;
        textview.font=UIFont.systemFont(ofSize:UIScreen.main.bounds.width*16/393);
        textview.isEditable=false;
        textview.isScrollEnabled=false;
        setTextColor(textview);
        setBackgroundColor(textview);
        setBoundingRect(linecount);
        setPosition();
        textview.layer.masksToBounds=true;
        textview.layer.cornerRadius=25;
    }

    func show(){
        let duration=getDuration();
        DispatchQueue.main.asyncAfter(deadline:DispatchTime.now()+duration){
            self.textview.removeFromSuperview();
        }
        parent.addSubview(textview);
    };

    private func setTextColor(_ textview:UITextView){
        let color=props["color"] as? String ?? "black";
        textview.textColor=getUIColorFromHex(color);
    }

    private func setBackgroundColor(_ textview:UITextView){
        let backgroundColor=props["backgroundColor"] as? String ?? "white";
        textview.backgroundColor=getUIColorFromHex(backgroundColor);
        textview.alpha=0.9;
    }

    private func setBoundingRect(_ linecount:Int){
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
        let align=props["verticalAlign"] as? String ?? "bottom";
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

    private func getDuration()->Double{
        let lasting=(props["lasting"] as? String) ?? "short";
        return lasting=="long" ? 3.5 : 2.0;
    }

}
