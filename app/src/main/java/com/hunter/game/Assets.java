package com.hunter.game;

import com.wxyz.framework.gl.Texture;
import com.wxyz.framework.gl.TextureRegion;
import com.wxyz.framework.impl.GLGame;

/**
 * Created by weiyan on 2016/10/29.
 */

public class Assets {
    public static Texture main_page;
    public static TextureRegion page_region;

    public static void load(GLGame game){
        main_page = new Texture(game,"page1.png");
        page_region = new TextureRegion(main_page,0,0,1080,1920);
    }

    public static void reload(){
        main_page.reload();
    }
}
