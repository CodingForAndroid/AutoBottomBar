package jorge.com.autobottombar.bean;

/**
 * @author zj on 2017-2-24.
 */

public   class BottomMenuItem {

    public BottomMenuItem(String title, String choosePic, String defaultPic, String pointType, String pointValue, String isHighlight, String chooseColor, String defaultColor) {
        this.title = title;
        this.choosePic = choosePic;
        this.defaultPic = defaultPic;
        this.pointType = pointType;
        this.pointValue = pointValue;
        this.isHighlight = isHighlight;
        this.chooseColor = chooseColor;
        this.defaultColor = defaultColor;
    }

    // tab 按钮 文字
    public String title = "" ;
    // 选中 图标 地址
    public String choosePic = "" ;
    // 默认 图标 地址
    public String defaultPic = "" ;
    // 跳转 类型
    public String pointType = "" ;
    // 跳转 活动参数
    public String pointValue = "" ;
    // 是否 中间展示的大图标
    public String isHighlight = "" ;
    // 选中 文字颜色
    public String chooseColor = "" ;
    // 默认 文字颜色
    public String defaultColor = "" ;
}
