package com.hunter.game;

import com.wxyz.framework.gl.Font;
import com.wxyz.framework.gl.Texture;
import com.wxyz.framework.gl.TextureRegion;
import com.wxyz.framework.impl.GLGame;

/**
 * 测向机所需要的资源
 * Created by weiyan on 2016/11/13.
 */

public class GameAssets {
    public static Texture battle_back;
    public static Texture team_back;
    public static Texture font_texture;

    public static TextureRegion page_region;
    public static TextureRegion search_button_up;
    public static TextureRegion search_button_down;
    public static TextureRegion search_button_busy;
    public static TextureRegion connecting;
    public static TextureRegion frequency_pointer;
    public static TextureRegion fox_remained;
    public static TextureRegion fox_hunted;
    public static TextureRegion fox_red;
    public static TextureRegion fox_blue;
    public static Font font;

    public static void load(GLGame game) {
        battle_back = new Texture(game,"gameUI1.png");
        team_back = new Texture(game, "gameUI2.png");
        font_texture = new Texture(game, "font.png");

        page_region = new TextureRegion(battle_back,0,0,1080,1920);
        search_button_up = new TextureRegion(battle_back,1080,0,628,628);
        search_button_busy = new TextureRegion(battle_back,1080,628,628,628);
        search_button_down = new TextureRegion(battle_back,1080,1256,628,628);
        connecting = new TextureRegion(battle_back,0,1920,1080,128);
        frequency_pointer = new TextureRegion(battle_back,1708,0,224,224);
        fox_remained = new TextureRegion(battle_back,1708,628,93,96);
        fox_hunted = new TextureRegion(battle_back,1708,724,93,96);
        fox_red = fox_hunted;
        fox_blue = new TextureRegion(team_back,1708,820,93,96);

        font = new Font(font_texture,0,0,16,32,48);
    }

    public static void reload() {
        battle_back.reload();
        team_back.reload();
        font_texture.reload();
    }
}
