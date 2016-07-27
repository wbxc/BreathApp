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
    private float zi_float = 150f ;












    public SceneManager(SimpleBaseGameActivity mContext, ResourceManager mResourceManager, ParallaxBackground mParallaxBackground) {
        this.mContext = mContext;
        this.mResourceManager = mResourceManager;
        this.mParallaxBackground = mParallaxBackground;
    }
    public Scene createScene(int totalTimes,String levelValue,String levelResult){
        Scene mScene = new Scene() ;
        VertexBufferObjectManager vbo = mContext.getVertexBufferObjectManager() ;

        Sprite backgroundSprite = new Sprite(0,0,mResourceManager.mBackgroundTextureRegion,vbo) ;
        mParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(1,backgroundSprite));
        mScene.setBackground(mParallaxBackground);
        mScene.setBackgroundEnabled(true);


        //bird
        float birdStartXOffset = (CommonValues.CAMERA_WIDTH/2) - (Bird.BIRD_WIDTH/2) ;
        float birdYOffset = (CommonValues.CAMERA_HEIGHT/2)-(Bird.BIRD_HEIGHT/2)-130 ;
        mBird = new Bird(birdStartXOffset,birdYOffset,mContext.getVertexBufferObjectManager(),mScene) ;
        //首先加载资源
        //其次将资源加载到场景中
        //200  172
        //250 426
        mInstructionsSprite = new Sprite(0,0,332,140,mResourceManager.mInstructionsTexture,mContext.getVertexBufferObjectManager()) ;
        mInstructionsSprite.setZIndex(3);
        mScene.attachChild(mInstructionsSprite);
        centerSprite(mInstructionsSprite) ;
        mInstructionsSprite.setY(mInstructionsSprite.getY()+20);









        tipsSprite = new Sprite(0,0,70,112,mResourceManager.tipsTextureRegion,mContext.getVertexBufferObjectManager()) ;
        tipsSprite.setZIndex(3);
        mScene.attachChild(tipsSprite);
        tipsSprite.setX(50f);
        tipsSprite.setY(CommonValues.CAMERA_HEIGHT - 310f);


        displayText = new Text(0,0,mResourceManager.displayFont,"对准呼吸器，缓慢吹气\n让小鸟顺利通过障碍物",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager()) ;
        displayText.setZIndex(3);
        // centerText(displayText);
        displayText.setX(140f);
        displayText.setY((CommonValues.CAMERA_HEIGHT-290f));
        mScene.attachChild(displayText);







        // 初始界面的准备图案
        initSprite = new Sprite(0,0,93,380,mResourceManager.initTextureRegion,mContext.getVertexBufferObjectManager()) ;
        initSprite.setZIndex(3);
        mScene.attachChild(initSprite);
        initSprite.setX(CommonValues.CAMERA_WIDTH-(150f));
        initSprite.setY(CommonValues.CAMERA_HEIGHT - 380f);






        breathSprite = new Sprite(0,0,207,317,mResourceManager.breathITextureRegion,mContext.getVertexBufferObjectManager()) ;
        breathSprite.setZIndex(3);
        mScene.attachChild(breathSprite);
        breathSprite.setX(CommonValues.CAMERA_WIDTH-220f);
        breathSprite.setY(CommonValues.CAMERA_HEIGHT - 320f);
        breathSprite.setVisible(false);

        inhaleSprite = new Sprite(0,0,207,317,mResourceManager.inhaleITextureRegion,mContext.getVertexBufferObjectManager()) ;
        inhaleSprite.setZIndex(3);
        mScene.attachChild(inhaleSprite);
        inhaleSprite.setX(CommonValues.CAMERA_WIDTH-220f);
        inhaleSprite.setY(CommonValues.CAMERA_HEIGHT - 320f);
        inhaleSprite.setVisible(false);



        displayDifficultyText = new Text(0,0,mResourceManager.displayDifficultyText,"难度系数",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager())  ;
        displayDifficultyText.setZIndex(3);
        //rightText(displayDifficultyText);
        displayDifficultyText.setX(50f);
        displayDifficultyText.setY((CommonValues.CAMERA_HEIGHT-zi_float));
        mScene.attachChild(displayDifficultyText);

        levelText = new Text(0,0,mResourceManager.levelFont,"Level",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager())  ;
        levelText.setZIndex(3);
        mScene.attachChild(levelText);
        levelText.setX(50f);
        levelText.setY(CommonValues.CAMERA_HEIGHT-107);

        levelFlagText = new Text(0,0,mResourceManager.levelFlagFont,levelResult,new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager())  ;
        levelFlagText.setZIndex(3);
        mScene.attachChild(levelFlagText);
        levelFlagText.setX(50f);
        levelFlagText.setY(CommonValues.CAMERA_HEIGHT-82);


        levelValueText = new Text(0,0,mResourceManager.levelValueFont,levelValue,new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager())  ;
        levelValueText.setZIndex(3);
        mScene.attachChild(levelValueText);
        levelValueText.setX(120f);
        levelValueText.setY(CommonValues.CAMERA_HEIGHT-110);


        firstLine = new Sprite(0,0,2,100,mResourceManager.mFirstTextureRegion,mContext.getVertexBufferObjectManager()) ;
        firstLine.setZIndex(3);
        mScene.attachChild(firstLine);
        setFirstLine(firstLine);





        displayTimeText = new Text(0,0,mResourceManager.displayTimeFontText,"剩余时间",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager()) ;
        displayTimeText.setZIndex(3);
        //leftText(displayTimeText);
        mScene.attachChild(displayTimeText);
        displayTimeText.setX(230f);
        displayTimeText.setY(CommonValues.CAMERA_HEIGHT-zi_float);

        displayTime = new Text(0,0,mResourceManager.displayTimeFont,String.valueOf(totalTimes),new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager()) ;
        displayTime.setZIndex(3);
        //leftText(displayTime);
        mScene.attachChild(displayTime);
        displayTime.setX(230f);
        displayTime.setY(CommonValues.CAMERA_HEIGHT-110);

        secondLine = new Sprite(0,0,2,100,mResourceManager.mSecondTextureRegion,mContext.getVertexBufferObjectManager()) ;
        secondLine.setZIndex(3);
        mScene.attachChild(secondLine);
        setSecondLine(secondLine);








        displayGroupsText = new Text(0,0,mResourceManager.displayGroupsFontText,"剩余组数",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager()) ;
        displayGroupsText.setZIndex(3);

        //centerText(displayGroupsText);
        mScene.attachChild(displayGroupsText);
        displayGroupsText.setX(385f);
        displayGroupsText.setY(CommonValues.CAMERA_HEIGHT-zi_float);


        displayGroups = new Text(0,0,mResourceManager.displayGroupsFont,"30",new TextOptions(HorizontalAlign.CENTER),mContext.getVertexBufferObjectManager()) ;
        displayGroups.setZIndex(3);
        //centerText(displayGroups);
        mScene.attachChild(displayGroups);
        displayGroups.setX(385f);
        displayGroups.setY(CommonValues.CAMERA_HEIGHT-110f);










        return mScene;
    }
    private static void centerSprite(Sprite sprite){

        sprite.setX((CommonValues.CAMERA_WIDTH/2) - (sprite.getWidth()/2));
        sprite.setY((CommonValues.CAMERA_HEIGHT/2) - (sprite.getHeight()/2) -280);
    }

    private void setFirstLine(Sprite sprite){

        //sprite.setX(((CommonValues.CAMERA_WIDTH)/3)-(sprite.getWidth()/2));
        sprite.setX(195f) ;
        sprite.setY((CommonValues.CAMERA_HEIGHT-zi_float));

    }
    private void setSecondLine(Sprite sprite){

        //sprite.setX(((CommonValues.CAMERA_WIDTH)/3*2)-(sprite.getWidth()/2));
        sprite.setX(358f) ;
        sprite.setY((CommonValues.CAMERA_HEIGHT-zi_float));

    }


    private void setThrid(Sprite sprite){

        sprite.setX(((CommonValues.CAMERA_WIDTH)/6*5)-(sprite.getWidth()/2));
        sprite.setY((CommonValues.CAMERA_HEIGHT-135));

    }


    /**
     * 中间文字显示
     */
    public void  displayCurrentText(String mes){
        displayText.setText(mes);

    }

    /**
     * 剩余的组数
     * @param remain
     */
    public void displayCurrentGroups(int remain){
        displayGroups.setText(""+remain);
    }


    public void displayLevel(String levelValue,String level){

        levelValueText.setText(levelValue);
        levelFlagText.setText(level);
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





}
