package com.hhd.breath.app.andengine;

import com.hhd.breath.app.main.ui.HealthCheck;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * Created by familylove on 2016/6/1.
 */
public class HealthBird {


    private float birdXOffset ;
    private float birdYOffset ;
    private AnimatedSprite mSprite ;

    private static BuildableBitmapTextureAtlas mBirdBuildableBitmapTextureAtlas ;
    private static TiledTextureRegion mBirdTiledTextureRegion ;
    public static final float BITMAP_WIDTH = 1047f ;
    public static final float BITMAP_HEIGHT = 903f ;

    public static final float BIRD_WIDTH = 55.8f ;
    public static final float BIRD_HEIGHT = 40f ;



    public HealthBird(float birdXOffset , float birdYOffset , VertexBufferObjectManager mVertexBufferObjectManager, Scene mScene){
        this.birdXOffset = birdXOffset ;
        this.birdYOffset = birdYOffset ;
        mSprite = new AnimatedSprite(birdXOffset,birdYOffset,55.8f,40,mBirdTiledTextureRegion,mVertexBufferObjectManager) ;
        mSprite.animate(25);
        mSprite.setZIndex(2);
        mScene.attachChild(mSprite);

    }

    /**
     * 加载资源
     * @param activity
     */
    public static void onCreateResources(SimpleBaseGameActivity activity){


        mBirdBuildableBitmapTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),(int)BITMAP_WIDTH,(int)BITMAP_HEIGHT, TextureOptions.NEAREST) ;
        mBirdTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBirdBuildableBitmapTextureAtlas,activity,"birdmap.png",3,3) ;
        try {
            mBirdBuildableBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,0,0)) ;
            mBirdBuildableBitmapTextureAtlas.load();
        }catch (Exception e){

        }
    }

    /**
     *移动位置
     */
    public float move(float px,float py){
        mSprite.setPosition(px,py-(HealthBird.BIRD_WIDTH/2));
        return py ;
    }


    public AnimatedSprite getSprite(){
        return  mSprite ;
    }
}
