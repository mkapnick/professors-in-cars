import factory.MenuFactory;
import factory.SceneFactory;
import factory.StoryFactory;
import io.ResourceFinder;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import model.Environment;
import model.EventNode;
import model.Script;
import model.View;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import scene.io.DialogueReader;
import scene.visual.Scene;
import scene.visual.content.MenuContent;
import scene.visual.content.SceneContent;
import view.MenuView;
import view.StoryView;
import visual.VisualizationView;
import visual.dynamic.described.Stage;
import app.AbstractMultimediaApp;

/**
 * Created with IntelliJ IDEA.
 * User: michaelk18
 * Date: 11/15/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */

public class FinalApp extends AbstractMultimediaApp
{
    private Stage stage, stage2;
    private VisualizationView stageView, stageView2;
    private final int NUM_SCENES = 5;
    private EventNode<SceneContent> content;
    private EventNode<MenuContent> menuContent;
    private MenuView menuView;

    public FinalApp() throws ParserConfigurationException, SAXException, IOException
    {
        stage = new Stage(50);
        stage2 = new Stage(50);
        stageView2 = stage2.getView();
        stageView = stage.getView();
        stageView.setBackground(new Color(158,209,144));
        //stage.setBackground();
        stageView.setBounds(0, 0, 640, 480);
        stageView2.setBounds(0, 480, 640, 480);
        
        DialogueReader reader = new DialogueReader("Chris", ResourceFinder.createInstance());
		Document xml = reader.getXML();
		menuContent = MenuFactory.createDialogue("Chris", xml);
    }

    public void init()
    {

        startUp();
        JPanel contentPane = (JPanel) rootPaneContainer.getContentPane();
        contentPane.setSize(600,400);

        contentPane.add(stageView);
        contentPane.add(stageView2);
        System.out.println("Before stage start");
        stage.start();
        stage2.start();
        System.out.println("stage started");
        contentPane.setVisible(true);
        System.out.println("After");

    }

    public void startUp()
    {

        ResourceFinder          finder;
        BufferedReader          br;
        InputStream             is;
        Scene []                scenes;
        StoryView               storyView;

        finder = ResourceFinder.createInstance(this);
        scenes = new Scene[NUM_SCENES];
        System.out.println("JUST BEFORE SCENE FACTORY CALL");

        //Construct all possible scenes
        scenes[0]   = SceneFactory.createScene(Environment.INTRO, View.BIRDSEYE,
                                                    Script.INTRO_SCRIPT, finder, "introScene.xml");
        scenes[0].setBackgroundColor(new Color(158,209,144));


        scenes[1]   = SceneFactory.createScene(Environment.CITY, View.SIDEVIEW,
                                                    Script.CITY_SCRIPT, finder, "cityScene.xml");
        scenes[1].setBackgroundColor(new Color(111,174,223));


        scenes[2]   = SceneFactory.createScene(Environment.FOREST, View.SIDEVIEW,
                                                    Script.FOREST_SCRIPT, finder, "forestScene.xml");
        scenes[2].setBackgroundColor(new Color(0,0,0));


        scenes[3]   = SceneFactory.createScene(Environment.MOUNTAINS, View.SIDEVIEW,
                                                    Script.MOUNTAIN_SCRIPT, finder, "mountainScene.xml");
        scenes[3].setBackgroundColor(new Color(255,255,255));


        scenes[4]   = SceneFactory.createScene(Environment.FINAL, View.SIDEVIEW,
                                                    Script.FINAL_SCRIPT, finder, "finalScene.xml");
        scenes[4].setBackgroundColor(new Color(255,255,255));

        //build a tree that represents a story from these scenes
        content     = StoryFactory.createAStory(scenes);
        storyView   = new StoryView(content.getElement().getSceneController(),stageView, stage, content);
        menuView = new MenuView(menuContent.getElement().getController(), stageView);
        menuView.setMouseListeners(stageView2);
		menuView.setMouseMotionListeners(stageView2);
        stage2.add(menuView);
        stage.add(storyView);
        
    }
}
