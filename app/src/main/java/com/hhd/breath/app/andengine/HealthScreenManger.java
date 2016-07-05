package com.hhd.breath.app.andengine;

import com.hhd.breath.app.main.ui.HealthCheck;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

/**
 * Created by familylove on 2016/5/31.
 */
public class HealthScreenManger {

    private SimpleBaseGameActivity context ;
    private HealthResourceManager resourceManager ;
    private ParallaxBackground mParallaxBackground ;
    protected HealthBird mBird ;
    private Text mDisplayShow ;
    private Text mDisplayHeight ;
    private Text mDisplayDistance ;
    private Sprite lineSprite ;

    private Text mDisplayHeightText ;
    private Text mDisplayDistanceText ;





    public HealthScreenManger(SimpleBaseGameActivity context,HealthResourceManager resourceManager,ParallaxBackground mParallaxBackground){

        this.context = context ;
        this.mParallaxBackground = mParallaxBackground ;
        this.resourceManager = resourceManager ;
    }

    public Scene createScene(){
        Scene scene = new Scene() ;
        VertexBufferObjectManager vbo = context.getVertexBufferObjectManager() ;

        Sprite backgroundSprite = new Sprite(0,0,resourceManager.mBackgroundTextureRegion,vbo) ;
        mParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(1,backgroundSprite));

        scene.setBackground(mParallaxBackground);
        scene.setBackgroundEnabled(true);


        float birdStartXOffset = HealthCheck.CAMERA_WIDTH/4 - (HealthBird.BIRD_WIDTH/2) ;
        float birdYOffset = (HealthCheck.CAMERA_HEIGHT/2) - (HealthBird.BIRD_HEIGHT/2) ;
        mBird = new HealthBird(birdStartXOffset,birdYOffset,context.getVertexBufferObjectManager(),scene) ;
        //mBird.move(50,598-HealthBird.BIRD_HEIGHT) ;


        mDisplayShow = new Text(0,0,resourceManager.mDisplayFont,"对准呼吸器,使用最大力量\n将小鸟飞得最高、最远",new TextOptions(HorizontalAlign.CENTER),context.getVertexBufferObjectManager()) ;
        mDisplayShow.setZIndex(3);
        centerText(mDisplayShow);
        scene.attachChild(mDisplayShow);

        mDisplayHeight = new Text(0,0,resourceManager.mDisplayHeight,"  0  ",new TextOptions(HorizontalAlign.CENTER),context.getVertexBufferObjectManager()) ;
        mDisplayHeight.setZIndex(3);
        scene.attachChild(mDisplayHeight);
        leftText(mDisplayHeight);

        mDisplayHeightText = new Text(0,0,resourceManager.mDisplayHeightFont,"高度(峰速)",new TextOptions(HorizontalAlign.CENTER),context.getVertexBufferObjectManager()) ;
        mDisplayHeightText.setZIndex(3);
        scene.attachChild(mDisplayHeightText);
        leftText1(mDisplayHeightText);

        mDisplayDistanceText = new Text(0,0,resourceManager.mDisplayHeightFont,"距离(总量)",new TextOptions(HorizontalAlign.CENTER),context.getVertexBufferObjectManager()) ;
        mDisplayDistanceText.setZIndex(3);
        scene.attachChild(mDisplayDistanceText);
        rightText2(mDisplayDistanceText);



        mDisplayDistance = new Text(0,0,resourceManager.mDisplayDistance,"   0  ",new TextOptions(HorizontalAlign.CENTER),context.getVertexBufferObjectManager()) ;
        mDisplayDistance.setZIndex(3);
        scene.attachChild(mDisplayDistance);
        rightText(mDisplayDistance);


        lineSprite = new Sprite(0,0,2,100,resourceManager.lineTextureRegion,context.getVertexBufferObjectManager()) ;
        lineSprite.setZIndex(3);
        scene.attachChild(lineSprite);
        centerSprite(lineSprite);





        return scene ;
    }


    private void  centerText(Text text){

        text.setX(HealthCheck.CAMERA_WIDTH/2-text.getWidth()/2);
        text.setY(HealthCheck.CAMERA_HEIGHT -220-text.getHeight()/2);

    }

    private void centerSprite(Sprite sprite){

        sprite.setPosition(HealthCheck.CAMERA_WIDTH/2-(sprite.getWidth()/2),HealthCheck.CAMERA_HEIGHT-160);

    }

    private void leftText(Text text){
        text.setPosition(HealthCheck.CAMERA_WIDTH/4-(text.getWidth()/2),HealthCheck.CAMERA_HEIGHT-160);
    }
    private void rightText(Text text){
        text.setPosition(HealthCheck.CAMERA_WIDTH/4*3-(text.getWidth()/2),HealthCheck.CAMERA_HEIGHT-160);
    }

    private void leftText1(Text text){
        text.setPosition(HealthCheck.CAMERA_WIDTH/4-(text.getWidth()/2),HealthCheck.CAMERA_HEIGHT-100);
    }
    private void rightText2(Text text){
        text.setPosition(HealthCheck.CAMERA_WIDTH/4*3-(text.getWidth()/2),HealthCheck.CAMERA_HEIGHT-100);
    }


    public void setMessage(float value){
        mDisplayHeight.setText(""+value);
    }

    public Text getDisplayHeight(){
        return mDisplayHeight ;
    }

    public Text getmDisplayDistance(){
        return mDisplayDistance ;
    }


    public void setDistance(float value){
        mDisplayDistance.setText(""+(int)value);
    }
    public HealthBird getBird(){
        return  mBird ;
    }
}
