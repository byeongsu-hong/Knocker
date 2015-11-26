package controllers;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.lang.String;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;

/**
 *
 * Created by frostornge on 15. 11. 24.
 */
public class GameController implements Initializable{

    /** FXML resources **/
    @FXML private AnchorPane View;
    @FXML private ProgressBar doorHealth;
    @FXML private Button toolsBtn;
    @FXML private ListView<HBox> toolsPanel;
    @FXML private Label currentMoney;
    @FXML private Label toolInfo;
    @FXML private Label notification;

    /** Variables **/
    private Doors currentDoor;
    private Tools currentTool;
    private Map<String, Doors> doors;
    private Map<String, Tools> tools;
    private boolean isTimeToOpen = true;
	private List<Doors> doorOrder = new ArrayList<>();

	private ImageView door;
	private Iterator<Doors> iterator;

    // 문의 체력을 체크하고, 만약 문의 체력이 0 이하일 경우 문을 없앤다.
    private void checkHealth() {
        if(doorHealth.getProgress() <= 1.3877787807814457E-16) {
            showNotification("\'" + currentDoor.getDoorName() + "\'이/가 파괴되었다!");
            View.getChildren().remove(door);
			if(iterator.hasNext()) {
				currentDoor = iterator.next();
				door.setImage(new Image(String.valueOf(getClass().getResource(currentDoor.getDoorFile()))));
				View.getChildren().add(door);
			}
            doorHealth.setProgress(1);

        }
    }

    // 노크를 했을 때 처리하는 메소드.
    public void handleKnock() {
        currentDoor.addDamage(currentTool.getPower());
        doorHealth.setProgress(currentDoor.getCurrentHealth() / currentDoor.getHealthFull());
        checkHealth();
    }

    /** 도구 핸들러들 **/
    public void handleFist() {
        currentTool = tools.get("손");
    }

    public void handleShoes() {
        currentTool = tools.get("신발");
    }

    public void handleHammer() {
        currentTool = tools.get("망치");
    }

    public void handleBenzene() {
        currentTool = tools.get("벤젠");
    }

    public void handleLight() {
        currentTool = tools.get("욱재 전광판");
    }

    public void handleBobby() {
        currentTool = tools.get("바비인형");
    }

    public void handleCurry() {
        currentTool = tools.get("깔리");
    }

    public void handleMath() {
        currentTool = tools.get("수학의 정석");
    }

    public void handleTeacher() {
        currentTool = tools.get("JMS");
    }
    /** 끗 **/

    // 툴 버튼을 눌렀을 때, Tool들이 있는 ListView를 보여주는 메소드.
    public void openSideMenu(ActionEvent event) {
        TranslateTransition translateBtn;
        TranslateTransition translatePanel;

        if(isTimeToOpen) {
            translateBtn = new TranslateTransition();
            translateBtn.setDuration(new Duration(500));
            translateBtn.setNode(toolsBtn);
            translateBtn.setToX(toolsBtn.getTranslateX() - 200);
            translateBtn.setInterpolator(Interpolator.EASE_BOTH);

            translatePanel = new TranslateTransition();
            translatePanel.setDuration(new Duration(500));
            translatePanel.setNode(toolsPanel);
            translatePanel.setToX(toolsPanel.getTranslateX() - 200);
            translatePanel.setInterpolator(Interpolator.EASE_BOTH);

            isTimeToOpen = false;
        } else {
            translateBtn = new TranslateTransition();
            translateBtn.setDuration(new Duration(500));
            translateBtn.setNode(toolsBtn);
            translateBtn.setToX(toolsBtn.getTranslateX() + 200);
            translateBtn.setInterpolator(Interpolator.EASE_BOTH);

            translatePanel = new TranslateTransition();
            translatePanel.setDuration(new Duration(500));
            translatePanel.setNode(toolsPanel);
			translatePanel.setToX(toolsPanel.getTranslateX() + 200);
            translatePanel.setInterpolator(Interpolator.EASE_BOTH);

            isTimeToOpen = true;
        }

        translateBtn.play();
        translatePanel.play();
    }

    // 문이 파괴되었을 때나, 알려줄 것이 있을 때, 알림창을 띄워주는 메소드
    private void showNotification(String message) {
        notification.setText(message);

        TranslateTransition open = new TranslateTransition();
        open.setNode(notification);
        open.setToY(notification.getTranslateY() - 100);
        open.setDuration(new Duration(500));
        open.setInterpolator(Interpolator.EASE_BOTH);
        open.play();

        TranslateTransition close = new TranslateTransition();
        close.setNode(notification);
        close.setToY(notification.getTranslateY() + 100);
        close.setDuration(new Duration(500));
        close.setInterpolator(Interpolator.EASE_BOTH);

        open.setOnFinished(event -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            close.play();
        });
    }

    // FXML문서가 처음으로 Load되었을 때, 초기화 해주는 메소드.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        doorHealth.setProgress(1.0);

        tools = new HashMap<>();

        tools.put("손", new Tools(50.0, "../res/tools/fist.png", "손", "Fist"));
        tools.put("신발", new Tools(100.0, "../res/tools/shoes.png", "신발", "Shoes"));
        tools.put("바비인형", new Tools(200.0, "../res/tools/bobby.png", "바비인형", "Bobby"));
        tools.put("깔리", new Tools(300.0, "../res/tools/curry.png", "깔리", "Curry"));
        tools.put("망치", new Tools(500.0, "../res/tools/hammer.png", "망치", "Hammer"));
		tools.put("욱재 전광판", new Tools(1000.0, "../res/tools/light.png", "욱재 전광판", "Light"));
        tools.put("수학의 정석", new Tools(2000.0, "../res/tools/math.jpg", "수학의 정석", "Math"));
        tools.put("JMS", new Tools(5000.0, "../res/tools/teacher.jpg", "JMS의 손짓", "Teacher"));
        tools.put("벤젠", new Tools(100000.0, "../res/tools/benzene.png", "벤젠", "Benzene"));

        setListView(tools);

		// 초기 문 설정
		doorOrder.add(new Doors(3000, "../res/doors/wood.jpg", "나무문"));
		doorOrder.add(new Doors(5000, "../res/doors/glass.jpg", "유리문"));
//			new Doors(7000, "hardglass.png", "강화유리문"),
//			new Doors(3000, "stone.png", "돌문"),
//			new Doors(3000, "steel.png", "철문"),
//			new Doors(3000, "stomach.png", "명치"),
//			new Doors(3000, "secretary.png", "사무국장실문"),
//			new Doors(3000, "diamond.png", "다이아문")

		iterator = doorOrder.iterator();
		currentDoor = iterator.next();
        currentTool = tools.get("손");

		door = new ImageView();
		door.setFitWidth(225);
		door.setFitHeight(400);
		door.setLayoutX(296);
		door.setLayoutY(80);
		door.setImage(new Image(String.valueOf(getClass().getResource(currentDoor.getDoorFile()))));
		door.setOnMouseClicked(event -> {
			handleKnock();
			checkHealth();
		});

		View.getChildren().add(door);
    }

    // ListView 초기화 (Tool 메뉴)
    private void setListView(Map<String, Tools> tools) {
        toolsPanel.setLayoutX(800.0);
        toolsPanel.setPrefHeight(480.0);
        toolsPanel.setPrefWidth(200.0);

        for(Tools tool : tools.values()){

            // HBox 생성
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(20.0);

            // 마우스 이벤트 처리
            hBox.setOnMouseClicked(event -> {
                toolInfo.setText("Current Tool : " + tool.getToolName());
                try {
                    // tool의 toolMethod를 받아와서, 마우스 이벤트가 생겼을 때,
                    // 도구에 대응되는 핸들링 메소드를 호출시킨다.
                    Method m = getClass().getMethod(tool.getToolMethod());
                    m.invoke(this);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });

            // 도구 이미지 생성
            ImageView toolImage = new ImageView();
            toolImage.setFitHeight(50.0);
            toolImage.setFitWidth(50.0);
            toolImage.setImage(new Image(String.valueOf(getClass().getResource(tool.getToolFile()))));

            // 도구 이름 생성
            Label toolDesc = new Label();
            toolDesc.setText(tool.getToolName());

            // HBox에 도구 이미지와, 도구 이름을 넣어준다.
            hBox.getChildren().add(toolImage);
            hBox.getChildren().add(toolDesc);

            // 이렇게 만든 HBox를 toolsPanel(ListView)에 넣어준다.
            toolsPanel.getItems().add(hBox);
        }


    }

}
