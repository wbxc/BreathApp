package com.hhd.breath.app.andengine;

import android.util.Log;

import com.hhd.breath.app.CommonValues;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.algorithm.Spiral;

import butterknife.Bind;

/**
 * Created by familylove on 2016/5/24.
 * 场景管理
 */
public class SceneManager {
    private SimpleBaseGameActivity mContext ;
    private ResourceManager mResourceManager ;
    private ParallaxBackground mParallaxBackground ;
    protected Bird mBird ;
    protected Text displayText ;
    protected Sprite mInstructionsSprite ;
    protected Sprite mStartSpite ;
    protected Text displayTime ;
    protected Text displayGroups ;
    protected Text displayTimeText ;
    protected Text displayGroupsText ;
    protected Text displayDifficultyText ;
    protected Sprite firstLine ;
    protected Sprite secondLine ;
    //protected Sprite levelSprite ;
    protected Sprite tipsSprite ;
    protected Sprite initSprite ;

    protected Text levelText ;
    protected Text levelFlagText ;
    protected Text levelValueText ;

    protected Sprite inhaleSprite ;
    protected Sprite breathSprite ;












    public SceneManager(SimpleBaseGameActivity mContext, ResourceManager mResourceManager, ParallaxBackground mParallaxBackground) {
        this.mContext = mContext;
        this.mResourceManager = mResourceManager;
        this.mParallaxBackground = mParallaxBackground;
    }
    public Scene createScene(int totalTimes){
        Scene mScene = new Scene() ;
        VertexBufferObjectManager vbo = mContext.getVertexBufferObjectManager() ;

        Sprite backgroundSprite = new Sprite(0,0,mResourceManager.mBackgroundTextureRegion,vbo) ;
        mParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(1,backgroundSprite));
        mScene.setBackground(mParallaxBackground);
        mScene.setBackgroundEnabled(true);
        //bird
        float birdStartXOffset = (CommonValues.CAMERA_WIDTH/4) - (Bird.BIRD_WIDTH/2) ;
        float birdYOffset = (CommonValues.CAMERA_HEIGHT/2)-(Bird.BIRD_HEIGHT/2) ;
        mBird = new Bird(birdStartXOffset,birdYOffset,mContext.getVertexBufferObjectManager(),mScene) ;
        //首先加载资源
        //其次将资源加载到场景中
        //200  172
        //250 426
        mInstructionsSprite = new Sprite(0,0,150,255,mResourceManager.mInstructionsTexture,mContext.getVertexBufferObjectManager()) ;
        mInstructionsSprite.setZIndex(3);
        mScene.attachChild(mInstructionsSprite);
        centerSprite(mInstructionsSprite) ;
        mInstructionsSprite.setY(mInstructionsSprite.getY()+20);



        firstLine = new Sprite(0,0,2,80,mResourceManager.mFirstTextureRegion,mContext.getVertexBufferObjectManager()) ;
        firstLine.setZIndex(3);
        mScene.attachChild(firstLine);
        setFirstLine(firstLine);
        secondLine = new Sprite(0,0,2,80,mResourceManager.mSecondTextureRegion,mContext.getVertexBufferObjectManager()) ;
        secondLine.setZIndex(3);
        mScene.attachChild(secondLine);
        setSecondLine(secondLine);




        tipsSprite = new Sprite(0,0,50,80,mResourceManager.tipsTextureRegion,mContext.getVertexBufferObjectManager()) ;
        tipsSprite.setZIndex(3);
        mScene.attachChild(tipsSprite);
        tipsSprite.setX(20f);
        tipsSprite.setY(CommonValues.CAMERA_HEIGHT - 250f);


        inhaleSprite = new Sprite(0,0,120,200,mResourceManager.inhaleITextureRegion,mContext.getVertexBufferObjectManager()) ;
        inhaleSprite.setZIndex(3);
        mScene.attachChild(inhaleSprite);
        inhaleSprite.setX(390f);
        inhaleSprite.setY(CommonValues.CAMERA_HEIGHT - 250f);
        inhaleSprite.setVisible(false);



        breathSprite = new Sprite(0,0,120,200,mResourceManager.breathITextureRegion,mContext.getVertexBufferObjectManager()) ;
        breathSprite.setZIndex(3);
        mScene.attachChild(breathSprite);
        breathSprite.setX(390f);
        breathSprite.setY(CommonValues.CAMERA_HEIGHT - 250f);
        breathSprite.setVisible(false);




        // 初始界面的准备图案
        initSprite = new Sprite(0,0,60,274,mResourceManager.initTextureRegion,mContext.getVertexBufferObjectManager()) ;
        initSprite.setZIndex(3);
        mScene.attachChild(initSprite);
        initSprite.setX(410f);
        initSprite.setY(CommonValues.CAMERA_HEIGHT - 274f);



        displayText = new Text(0,0,mResourceManager.displayFont,"吸气时，最大限度地向\n外扩腹，胸部保持不动",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager()) ;
        displayText.setZIndex(3);
       // centerText(displayText);
        displayText.setX(80f);
        displayText.setY((CommonValues.CAMERA_HEIGHT-240));
        mScene.attachChild(displayText);


        displayTime = new Text(0,0,mResourceManager.displayTimeFont,getTimeLong(totalTimes),new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager()) ;
        displayTime.setZIndex(3);
        //leftText(displayTime);
        mScene.attachChild(displayTime);
        displayTime.setX(170f);
        displayTime.setY(CommonValues.CAMERA_HEIGHT-115);




        displayTimeText = new Text(0,0,mResourceManager.displayTimeFontText,"剩余时间",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager()) ;
        displayTimeText.setZIndex(3);
        //leftText(displayTimeText);
        mScene.attachChild(displayTimeText);
        displayTimeText.setX(170f);
        displayTimeText.setY(CommonValues.CAMERA_HEIGHT-145);







        displayGroups = new Text(0,(CommonValues.CAMERA_HEIGHT-140),mResourceManager.displayGroupsFont,"30",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager()) ;
        displayGroups.setZIndex(3);
        //centerText(displayGroups);
        mScene.attachChild(displayGroups);
        displayGroups.setX(300f);
        displayGroups.setY(CommonValues.CAMERA_HEIGHT-115);



        displayGroupsText = new Text(0,0,mResourceManager.displayGroupsFontText,"剩余组数",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager()) ;
        displayGroupsText.setZIndex(3);

        //centerText(displayGroupsText);
        mScene.attachChild(displayGroupsText);
        displayGroupsText.setX(300f);
        displayGroupsText.setY(CommonValues.CAMERA_HEIGHT-145);



        displayDifficultyText = new Text(0,(CommonValues.CAMERA_HEIGHT-70),mResourceManager.displayDifficultyText,"难度系数",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager())  ;
        displayDifficultyText.setZIndex(3);
        //rightText(displayDifficultyText);
        displayDifficultyText.setX(20f);
        displayDifficultyText.setY((CommonValues.CAMERA_HEIGHT-145));
        mScene.attachChild(displayDifficultyText);




        levelText = new Text(0,0,mResourceManager.levelFont,"Level",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager())  ;
        levelText.setZIndex(3);
        mScene.attachChild(levelText);
        levelText.setX(20f);
        levelText.setY(CommonValues.CAMERA_HEIGHT-115);

        levelFlagText = new Text(0,0,mResourceManager.levelFlagFont,"高级",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager())  ;
        levelFlagText.setZIndex(3);
        mScene.attachChild(levelFlagText);
        levelFlagText.setX(20f);
        levelFlagText.setY(CommonValues.CAMERA_HEIGHT-90);


        levelValueText = new Text(0,0,mResourceManager.levelValueFont,"24",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager())  ;
        levelValueText.setZIndex(3);
        mScene.attachChild(levelValueText);
        levelValueText.setX(90f);
        levelValueText.setY(CommonValues.CAMERA_HEIGHT-115);

        return mScene;
    }
    private static void centerSprite(Sprite sprite){

        sprite.setX((CommonValues.CAMERA_WIDTH/2) - (sprite.getWidth()/2));
        sprite.setY((CommonValues.CAMERA_HEIGHT/2) - (sprite.getHeight()/2));
    }

    private void setFirstLine(Sprite sprite){

        //sprite.setX(((CommonValues.CAMERA_WIDTH)/3)-(sprite.getWidth()/2));
        sprite.setX(150f) ;
        sprite.setY((CommonValues.CAMERA_HEIGHT-143));

    }
    private void setSecondLine(Sprite sprite){

        //sprite.setX(((CommonValues.CAMERA_WIDTH)/3*2)-(sprite.getWidth()/2));
        sprite.setX(280f) ;
        sprite.setY((CommonValues.CAMERA_HEIGHT-143));

    }


    private void setThrid(Sprite sprite){

        sprite.setX(((CommonValues.CAMERA_WIDTH)/6*5)-(sprite.getWidth()/2));
        sprite.setY((CommonValues.CAMERA_HEIGHT-135));

    }


    /**
     * 中间文字显示
     */
    public void  displayCurrentText(){
        displayText.setText("");

    }

    /**
     * 剩余的组数
     * @param remain
     */
    public void displayCurrentGroups(int remain){
        displayGroups.setText(""+remain);
    }

    /**
     * 剩余时间
     */
    public void displayCurrentTimes(int timeLong){
        displayTime.setText(String.valueOf(timeLong));
    }


    protected String getTimeLong(int timeLong) {

        if (timeLong>=0){
            StringBuffer sb = new StringBuffer();
            int fen = timeLong / 60;
            int second = timeLong % 60;
            if (fen <= 9) {
                sb.append("0" + fen + ":");
            } else {
                sb.append(String.valueOf(fen) + ":");
            }

            if (second <= 9) {
                sb.append("0" + second);
            } else {
                sb.append(String.valueOf(second));
            }
            return sb.toString();
        }
        return "00:00" ;
    }




    private void centerText(Text text){
        text.setX((CommonValues.CAMERA_WIDTH/2)-(text.getWidth()/2));
    }

    private void leftText(Text text){
        text.setX((CommonValues.CAMERA_WIDTH)/6 - (text.getWidth()/2));
    }

    private void rightText(Text text){
        text.setX((CommonValues.CAMERA_WIDTH)/6*5 -(text.getWidth()/2));
    }


    public void prepare(){


    }





}
