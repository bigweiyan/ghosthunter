package com.hunter.game;

import com.wxyz.framework.Sound;
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
    public static Texture item_texture;

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
    public static TextureRegion[] items_region;
    public static TextureRegion[] items_descript;
    public static TextureRegion[] toasts;
    public static final int TOAST_SIG_GET = 0;
    public static final int TOAST_NO_SIG_GET = 1;
    public static final int TOAST_ITEM_GET = 2;
    public static final int TOAST_UNDER_ITEM = 3;
    public static Font font;
    public static Sound long_sound,short_sound,empty_sound;

    public static void load(GLGame game) {
        battle_back = new Texture(game,"gameUI1.png");
        team_back = new Texture(game, "gameUI2.png");
        font_texture = new Texture(game, "font.png");
        item_texture = new Texture(game, "item.png");
        long_sound = game.getAudio().newSound("longsound.mp3");
        short_sound = game.getAudio().newSound("shortsound.mp3");
        empty_sound = game.getAudio().newSound("emptysound.mp3");

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

        items_region = new TextureRegion[5];
        items_region[0] = new TextureRegion(item_texture,0,0,194,194);
        items_region[1] = new TextureRegion(item_texture,194,0,194,194);
        items_region[2] = new TextureRegion(item_texture,388,0,194,194);
        items_region[3] = new TextureRegion(item_texture,0,194,194,194);
        items_region[4] = new TextureRegion(item_texture,194,194,194,194);
        items_descript = new TextureRegion[5];
        for(int i = 0; i < 3; i++) {
            items_descript[i] = new TextureRegion(item_texture, 582, 40*i, 188, 40);
        }
        for (int i = 3; i < 5; i++) {
            items_descript[i] = new TextureRegion(item_texture, 770, 40*(i%3), 188, 40);
        }
        toasts = new TextureRegion[4];
        for(int i = 0; i < 4; i++) {
            toasts[i] = new TextureRegion(item_texture, 582, 120+80*i, 442, 80);
        }

    }

    public static void reload() {
        battle_back.reload();
        team_back.reload();
        font_texture.reload();
        item_texture.reload();
    }
}
