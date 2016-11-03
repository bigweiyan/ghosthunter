package com.hunter.game;

import com.wxyz.framework.gl.Texture;
import com.wxyz.framework.gl.TextureRegion;
import com.wxyz.framework.impl.GLGame;

/**
 * Created by weiyan on 2016/10/29.
 */

public class Assets {
    public static Texture main_page;
    public static Texture battle_main_page;
    public static TextureRegion main_battle_button_released;
    public static TextureRegion main_team_button_released;
    public static TextureRegion main_battle_button_pressed;
    public static TextureRegion main_team_button_pressed;
    public static TextureRegion page_region;

    public static void load(GLGame game){
        main_page = new Texture(game,"page1.png");
        battle_main_page = new Texture(game,"page2.png");

        page_region = new TextureRegion(main_page,0,0,1080,1920);
        main_team_button_released = new TextureRegion(main_page,1080,0,524,164);
        main_battle_button_released = new TextureRegion(main_page,1080,164,524,164);
        main_team_button_pressed = new TextureRegion(main_page,1080,328,524,164);
        main_battle_button_pressed = new TextureRegion(main_page,1080,492,524,164);
    }

    public static void reload(){
        main_page.reload();
    }
}
