package com.hunter.game;

import com.wxyz.framework.gl.Texture;
import com.wxyz.framework.gl.TextureRegion;
import com.wxyz.framework.impl.GLGame;

/**
 * 存放游戏使用的图片资源的索引
 * Created by weiyan on 2016/10/29.
 */

public class Assets {
    static Texture main_page;
    static Texture battle_main_page;
    static Texture team_main_page;
    static TextureRegion main_battle_button_released;
    static TextureRegion main_team_button_released;
    static TextureRegion main_battle_button_pressed;
    static TextureRegion main_team_button_pressed;
    static TextureRegion page_region;

    static void load(GLGame game){
        main_page = new Texture(game,"page1.png");
        battle_main_page = new Texture(game,"page2.png");
        team_main_page = new Texture(game,"page3.png");

        page_region = new TextureRegion(main_page,0,0,1080,1920);
        main_team_button_released = new TextureRegion(main_page,1080,0,524,164);
        main_battle_button_released = new TextureRegion(main_page,1080,164,524,164);
        main_team_button_pressed = new TextureRegion(main_page,1080,328,524,164);
        main_battle_button_pressed = new TextureRegion(main_page,1080,492,524,164);
    }

    static void reload(){
        main_page.reload();
        battle_main_page.reload();
        team_main_page.reload();
    }
}