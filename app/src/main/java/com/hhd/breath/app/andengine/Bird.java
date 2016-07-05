package com.hhd.breath.app.andengine;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.IAnimationData;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.BuildableTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * Created by familylove on 2016/5/24.
 */
public class Bird {

    public static final float BITMAP_WIDTH = 1047f ;
    public static final float BITMAP_HEIGHT = 903f ;

    public static final float BIRD_WIDTH = 55.8f ;
    public static final float BIRD_HEIGHT = 40f ;
    protected static final  float MAX_DROP_SPEED = 6.0f ;

    protected static final float GRAVITY = 0.04f ;
    protected static final float FLAP_POWER = 3f ;

    protected static final float BIRD_MAX_FLAP_ANGLE = -20 ;
    protected static final float BIRD_MAX_DROP_ANGLE = 90 ;
    protected static final float FLAP_ANGLE_DRAG = 4.0f ;
    protected static final float BIRD_FLAP_ANGLE_POWER = 15f ;
    private AnimatedSprite mSprite  ;


    protected float mAcceleration = GRAVITY ;
    protected float mVerticalSpeed ;
    protected float mCurrentBirdAngle = BIRD_MAX_FLAP_ANGLE ;


    //BIRD
    private static BuildableBitmapTextureAtlas mBirdBitmapTextureAtlas ;
    private static TiledTextureRegion mBirdTextureRegion ;

    // sounds
    private static Sound mJumpSound ;


    private float mBirdYOffset , mBirdXOffset ;
    private Scene mScene ;

    public Bird(float mBirdXOffset , float mBirdYOffset, VertexBufferObjectManager mVertexBufferObjectManager , Scene mScene) {

        this.mBirdXOffset = mBirdXOffset ;
        this.mBirdYOffset = mBirdYOffset ;

        mSprite = new AnimatedSprite(mBirdXOffset,mBirdYOffset,55.8f,40,mBirdTextureRegion,mVertexBufferObjectManager) ;
        mSprite.animate(25);
        mSprite.setZIndex(2);
        this.mScene = mScene ;
        this.mScene.attachChild(mSprite);

    }

    public static void onCreateResources(SimpleBaseGameActivity activity){


        mBirdBitmapTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),(int) BITMAP_WIDTH,(int)BITMAP_HEIGHT, TextureOptions.NEAREST) ;
        mBirdTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBirdBitmapTextureAtlas,activity,"birdmap.png",3,3) ;
        try {
            mBirdBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,0,0)) ;
            mBirdBitmapTextureAtlas.load();
        }catch (Exception e){
        }

        try{

            mJumpSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(),activity,"jump.ogg") ;
        }catch (Exception e){

        }
    }

    public float move(float position){

        mSprite.setY(position);
        return  position ;
    }


    public boolean disappearBird(){
        this.mScene.detachChild(mSprite) ;

        return false;
    }

    public boolean showBird(){
        this.mScene.attachChild(mSprite);
        return true ;
    }


    public AnimatedSprite getSprite(){
        return  mSprite ;
    }
}
