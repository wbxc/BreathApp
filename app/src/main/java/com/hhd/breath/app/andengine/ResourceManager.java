package com.hhd.breath.app.andengine;

import android.graphics.Typeface;

import com.hhd.breath.app.R;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.StrokeFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import java.io.IOException;

/**
 * Created by familylove on 2016/5/24.
 * 资源文件导入
 */
public class ResourceManager {


    BitmapTextureAtlas mBackgroundBitmapTextureAtlas ;
    ITextureRegion mBackgroundTextureRegion ;
    TextureRegion mInstructionsTexture ;
    //TextureRegion mStartTexture ;
    TextureRegion mFirstTextureRegion ;
    TextureRegion mSecondTextureRegion ;
    //ITextureRegion levelTextureRegion ;

    //sounds
    Sound mScoreSound  ;
    Sound mDieSound ;
    Music mMusic ;
    Font displayFont ;
    Font displayTimeFont ;
    Font displayGroupsFont ;

    Font displayTimeFontText ;
    Font displayGroupsFontText ;
    Font displayDifficultyText ;
    private SimpleBaseGameActivity context ;
    public ITextureRegion tipsTextureRegion ;  //提示标示
    public ITextureRegion initTextureRegion ; // 初始

    Font levelFont ;
    Font levelValueFont ;
    Font levelFlagFont ;

    public ITextureRegion inhaleITextureRegion ;  // 吸气
    public ITextureRegion breathITextureRegion ; // 呼气









    public ResourceManager(SimpleBaseGameActivity context) {
        this.context = context;
    }
    public void createResources(int i){
        SoundFactory.setAssetBasePath("sound/");
        MusicFactory.setAssetBasePath("sound/");
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("img/");

        //背景
        mBackgroundBitmapTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(),718,1184, TextureOptions.NEAREST_PREMULTIPLYALPHA) ;
        //mBackgroundBitmapTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(),683,1280, TextureOptions.NEAREST_PREMULTIPLYALPHA) ;
        //mBackgroundBitmapTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(),1080,1920, TextureOptions.NEAREST_PREMULTIPLYALPHA) ;
        mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundBitmapTextureAtlas,context.getAssets(),"background480.png",0,0) ;
        //mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromResource(mBackgroundBitmapTextureAtlas,context.getResources(), R.mipmap.background1280,0,0) ;
        mBackgroundBitmapTextureAtlas.load();

        //开始指令
        //开始的图标
        BitmapTextureAtlas instructionsTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(),250,426,TextureOptions.BILINEAR) ;
        mInstructionsTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(instructionsTextureAtlas,context,"instructions.png",0,0) ;
        instructionsTextureAtlas.load();


        //first line
        BitmapTextureAtlas firstTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(),2,105,TextureOptions.BILINEAR) ;
        mFirstTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(firstTextureAtlas,context,"line.png",0,0) ;
        firstTextureAtlas.load();
        //second  line
        BitmapTextureAtlas secondTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(),2,105,TextureOptions.BILINEAR) ;
        mSecondTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(secondTextureAtlas,context,"line.png",0,0) ;
        secondTextureAtlas.load();

        Bird.onCreateResources(context);
        PipePair.onCreateResource(context);
        //显示
       // Typeface typeface = Typeface.DEFAULT ;
        Typeface typeface = Typeface.defaultFromStyle(Typeface.NORMAL) ;
        //Typeface typeface = Typeface.createFromAsset(context.getAssets(),"flappy.ttf") ;
        ITexture texture = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        displayFont = new StrokeFont(context.getFontManager(),texture,typeface,30,true, Color.WHITE,1,Color.WHITE) ;
        displayFont.load();

        ITexture displayTimeTexture = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        displayTimeFont = new StrokeFont(context.getFontManager(),displayTimeTexture,typeface,45,true,Color.WHITE,1,Color.WHITE) ;
        displayTimeFont.load();

        //剩余时长
        ITexture displayTimeTextTexture = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        displayTimeFontText = new StrokeFont(context.getFontManager(),displayTimeTextTexture,typeface,20,true,Color.BLACK,0,Color.BLACK) ;
        displayTimeFontText.load();

        ITexture displayGroupsTexture = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        displayGroupsFont = new StrokeFont(context.getFontManager(),displayGroupsTexture,typeface,45,true,Color.WHITE,1,Color.WHITE) ;
        displayGroupsFont.load();

        //剩余组数
        ITexture displayGroupsTextTexture = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        displayGroupsFontText = new StrokeFont(context.getFontManager(),displayGroupsTextTexture,typeface,20,true,Color.BLACK,0,Color.BLACK) ;
        displayGroupsFontText.load();

        //难度系数
        ITexture displayDifficultyTextTexture = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        displayDifficultyText = new StrokeFont(context.getFontManager(),displayDifficultyTextTexture,typeface,20,true,Color.BLACK,0,Color.BLACK) ;
        displayDifficultyText.load();

        ITexture levelITexture = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        levelFont = new StrokeFont(context.getFontManager(),levelITexture,typeface,24,true,Color.WHITE,1,Color.WHITE) ;
        levelFont.load();



        ITexture levelFlagITexture = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        levelFlagFont = new StrokeFont(context.getFontManager(),levelFlagITexture,typeface,25,true,Color.WHITE,1,Color.WHITE) ;
        levelFlagFont.load();

        ITexture levelValueITexture = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        levelValueFont = new StrokeFont(context.getFontManager(),levelValueITexture,typeface,45,true,Color.WHITE,1,Color.WHITE) ;
        levelValueFont.load();






        //tips
        BitmapTextureAtlas tipsTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(),103,164,TextureOptions.BILINEAR) ;
        tipsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tipsTextureAtlas,context.getAssets(),"icon_tips.png",0,0) ;
        tipsTextureAtlas.load();


        //inhale  吸气
        BitmapTextureAtlas inhaleTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(),300,460,TextureOptions.BILINEAR) ;
        inhaleITextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(inhaleTextureAtlas,context.getAssets(),"icon_inhale.png",0,0) ;
        inhaleTextureAtlas.load();


        //breath  呼气
        BitmapTextureAtlas breathTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(),300,460,TextureOptions.BILINEAR) ;
        breathITextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(breathTextureAtlas,context.getAssets(),"icon_breath.png",0,0) ;
        breathTextureAtlas.load();

        //tips
        BitmapTextureAtlas initTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(),112,460,TextureOptions.BILINEAR) ;
        initTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(initTextureAtlas,context.getAssets(),"icon_init.png",0,0) ;
        initTextureAtlas.load();




        //sounds
        try {
            mScoreSound = SoundFactory.createSoundFromAsset(context.getSoundManager(),context,"score.ogg") ;
            mDieSound = SoundFactory.createSoundFromAsset(context.getSoundManager(),context,"gameover.ogg") ;
        }catch (final IOException e){

        }

        try{
            mMusic = MusicFactory.createMusicFromAsset(context.getMusicManager(),context,"song.ogg") ;
            mMusic.setVolume(01.f);
            mMusic.setLooping(true);
        }catch (IOException e){

        }
    }
}
