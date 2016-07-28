package com.hhd.breath.app.andengine;

import com.hhd.breath.app.CommonValues;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.algorithm.Spiral;

/**
 * Created by familylove on 2016/5/24.
 */
public class PipePair {

    protected float PIPE_WIDTH  = CommonValues.CAMERA_WIDTH*0.18f ;
    private float PIPE_HEIGHT = PIPE_WIDTH * 0.46f ;
    private float height = CommonValues.SKY_HEIGHT ;

    // upper pipe
    private static TextureRegion mUpperPipeTexture ;
    private static TextureRegion mUpperPipeSectionTexture ;
    //lower pipe
    private static  TextureRegion mLowerPipeTexture ;
    private static  TextureRegion mLowerPipeSelectionTexture ;

    public static void onCreateResource(SimpleBaseGameActivity activity){

        //upper pipe
        BitmapTextureAtlas upperPipeTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),130,60, TextureOptions.BILINEAR) ;
        mUpperPipeTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperPipeTextureAtlas,activity,"pipeupper.png",0,0) ;
        upperPipeTextureAtlas.load();

        BitmapTextureAtlas upperPipeSectionTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),120,1,TextureOptions.BILINEAR) ;
        mUpperPipeSectionTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperPipeSectionTextureAtlas,activity,"pipesectionupper.png",0,0) ;
        upperPipeSectionTextureAtlas.load();





        //lower pipe
        BitmapTextureAtlas lowerPiperTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),130,60,TextureOptions.BILINEAR) ;
        mLowerPipeTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(lowerPiperTextureAtlas,activity,"pipelower.png",0,0) ;
        lowerPiperTextureAtlas.load();

        BitmapTextureAtlas lowerPiperSelectTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),120,1,TextureOptions.BILINEAR) ;
        mLowerPipeSelectionTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(lowerPiperSelectTextureAtlas,activity,"pipesectionlower.png",0,0) ;
        lowerPiperSelectTextureAtlas.load();
    }

    private float mOpeningHeight ;
    private float mCurrentPosition ;

    private VertexBufferObjectManager mVertexBufferObjectManager ;
    private Scene mScene ;

    private Sprite mUpperPipe ;
    private Sprite mUpperPipeSection ;
    private Sprite mLowerPipe ;
    private Sprite mLowerPipeSection ;

    private static final float PIPE_Y_OFFSET = CommonValues.CAMERA_WIDTH +200 ;

    public PipePair(float centerHeight, VertexBufferObjectManager mVertexBufferObjectManager, Scene mScene) {
        this.mOpeningHeight = centerHeight;
        this.mVertexBufferObjectManager = mVertexBufferObjectManager;
        this.mScene = mScene;


        mUpperPipeSection = new Sprite(PIPE_Y_OFFSET+3,0,
                                    CommonValues.PAIR_WIDTH,CommonValues.C_VALUE_TOP-CommonValues.PAIR_WIDTH_CENTER_HEIGHT,
                                    mUpperPipeSectionTexture,mVertexBufferObjectManager) ;
        mUpperPipeSection.setZIndex(1);
        mScene.attachChild(mUpperPipeSection);



        mUpperPipe = new Sprite(PIPE_Y_OFFSET,CommonValues.C_VALUE_TOP-CommonValues.PAIR_WIDTH_CENTER_HEIGHT,
                                CommonValues.PAIR_WIDTH_CENTER,CommonValues.PAIR_WIDTH_CENTER_HEIGHT,
                mUpperPipeTexture,mVertexBufferObjectManager) ;
        //81
        mUpperPipe.setZIndex(1);
        mScene.attachChild(mUpperPipe);







        mLowerPipe = new Sprite(PIPE_Y_OFFSET,CommonValues.S_VALUE,CommonValues.PAIR_WIDTH_CENTER,CommonValues.PAIR_WIDTH_CENTER_HEIGHT,mLowerPipeTexture,mVertexBufferObjectManager) ;
        mLowerPipe.setZIndex(1);
        mScene.attachChild(mLowerPipe);

        //644

/*        mLowerPipeSection = new Sprite(PIPE_Y_OFFSET+3, centerHeight+(CommonValues.CONTROLLER_VALUE+CommonValues.PAIR_WIDTH_CENTER_HEIGHT),
                CommonValues.PAIR_WIDTH,
                (height-(centerHeight+CommonValues.CONTROLLER_VALUE+CommonValues.PAIR_WIDTH_CENTER_HEIGHT)),
                mLowerPipeSelectionTexture,
                mVertexBufferObjectManager) ;*/

        mLowerPipeSection = new Sprite(PIPE_Y_OFFSET+3,CommonValues.S_VALUE+CommonValues.PAIR_WIDTH_CENTER_HEIGHT,
                CommonValues.PAIR_WIDTH,
                CommonValues.S_VALUE_LEVEL-CommonValues.PAIR_WIDTH_CENTER_HEIGHT,
                mLowerPipeSelectionTexture,
                mVertexBufferObjectManager) ;



        mLowerPipeSection.setZIndex(1);
        mScene.attachChild(mLowerPipeSection);
        mScene.sortChildren();
    }

    public boolean isOnScreen(){
        if (mUpperPipe.getX() <-200){
            return false ;
        }
        return true ;
    }

    public void move(float offset){

        mUpperPipe.setPosition(mUpperPipe.getX()- offset , mUpperPipe.getY());
        mUpperPipeSection.setPosition(mUpperPipeSection.getX()-offset,mUpperPipeSection.getY());

        mLowerPipe.setPosition(mLowerPipe.getX()-offset,mLowerPipe.getY());
        mLowerPipeSection.setPosition(mLowerPipeSection.getX()-offset,mLowerPipeSection.getY());

    }
    public void destroy(){

        mScene.detachChild(mUpperPipe) ;
        mScene.detachChild(mUpperPipeSection) ;
        mScene.detachChild(mLowerPipe) ;
        mScene.detachChild(mLowerPipeSection) ;
    }

    public int collidesWidth(Sprite bird){

        if (mUpperPipe.collidesWith(bird)) return  1 ;
        if (mUpperPipeSection.collidesWith(bird)) return 2 ;

        if (mLowerPipe.collidesWith(bird)) return  3 ;
        if (mLowerPipeSection.collidesWith(bird)) return 4 ;

        return 0 ;
    }

    boolean counted = false ;

    public boolean isCleared(float birdXOffset){
        if (!counted){
            if (mUpperPipe.getX() < (birdXOffset - (Bird.BIRD_WIDTH/2))){
                counted = true ;
                return  false ;
            }
        }
        return false ;
    }
}
