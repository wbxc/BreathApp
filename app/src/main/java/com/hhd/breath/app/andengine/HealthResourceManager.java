package com.hhd.breath.app.andengine;

import android.graphics.Typeface;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.StrokeFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

/**
 * Created by familylove on 2016/5/31.
 */
public class HealthResourceManager {


    protected  BitmapTextureAtlas mBackgroundBitmapTextureAtlas ;
    protected ITextureRegion mBackgroundTextureRegion ;
    protected Font mDisplayFont ;
    protected Font mDisplayHeight ;
    protected Font mDisplayDistance ;
    public ITextureRegion lineTextureRegion ;


    protected Font mDisplayHeightFont ;
    protected Font mDisplayDistanceFont ;





    private SimpleBaseGameActivity context ;
    public HealthResourceManager(SimpleBaseGameActivity context){
        this.context = context ;
    }


    public void createResources(){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("img/");
        mBackgroundBitmapTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(),718,1184, TextureOptions.NEAREST_PREMULTIPLYALPHA) ;
        mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundBitmapTextureAtlas,context.getAssets(),"background480.png",0,0) ;
        mBackgroundBitmapTextureAtlas.load();
        HealthBird.onCreateResources(context);

        Typeface typeface = Typeface.defaultFromStyle(Typeface.NORMAL) ;

        ITexture mDisplayShowTexture = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        mDisplayFont = new StrokeFont(context.getFontManager(),mDisplayShowTexture,typeface,25,true, Color.WHITE,1,Color.WHITE) ;
        mDisplayFont.load();

        ITexture mDisplayHeightTexture = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        mDisplayHeight = new StrokeFont(context.getFontManager(),mDisplayHeightTexture,typeface,45,true,Color.WHITE,1,Color.WHITE)  ;
        mDisplayHeight.load();

        ITexture mHeightText = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        mDisplayHeightFont = new StrokeFont(context.getFontManager(),mHeightText,typeface,25,true,Color.WHITE,1,Color.WHITE) ;
        mDisplayHeightFont.load();


       /* protected Font mDisplayHeightFont ;
        protected Font mDisplayDistanceFont ;
        */


        ITexture mDisplayDistanceTexture = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        mDisplayDistance = new StrokeFont(context.getFontManager(),mDisplayDistanceTexture,typeface,45,true,Color.WHITE,1,Color.WHITE) ;
        mDisplayDistance.load();

        ITexture mDistanceText = new BitmapTextureAtlas(context.getTextureManager(),256,256,TextureOptions.BILINEAR) ;
        mDisplayDistanceFont = new StrokeFont(context.getFontManager(),mDistanceText,typeface,25,true,Color.WHITE,1,Color.WHITE) ;
        mDisplayDistanceFont.load();


        //竖线
        BitmapTextureAtlas  lineTextureAtlas = new BitmapTextureAtlas(context.getTextureManager(),2,150,TextureOptions.BILINEAR) ;
        lineTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(lineTextureAtlas,context.getAssets(),"line.png",0,0) ;
        lineTextureAtlas.load();

    }
}
