package controllers;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
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
    @FXML private Label doorName;
    @FXML private ListView<String> notification;


    /** Variables **/
    private Doors currentDoor;
    private Tools currentTool;
    private Map<String, Tools> tools;
    private boolean isTimeToOpen = true;
	private List<Doors> doorOrder = new ArrayList<>();

	private ImageView door;
	private Iterator<Doors> iterator;

    // 문의 체력을 체크하고, 만약 문의 체력이 0 이하일 경우 문을 없앤다.
    private void checkHealth() {

		Doors previousDoor = currentDoor;
        if(doorHealth.getProgress() <= 1.3877787807814457E-16) {
			showNotification("\'" + previousDoor.getDoorName() + "\'이/가 파괴되었다!");
			View.getChildren().remove(door);

			if(iterator.hasNext()) {
				currentDoor = iterator.next();
                doorName.setText(currentDoor.getDoorName());
                door.setImage(new Image(String.valueOf(getClass().getResource(currentDoor.getDoorFile()))));
                door.setFitWidth(90);
				door.setFitHeight(160);
                System.out.println(door.getFitHeight() + ", " + door.getFitWidth());
                door.setLayoutX(355);
                door.setLayoutY(200);
                View.getChildren().add(door);
				spawnDoorAnim(door);
                System.out.println(door.getFitHeight() + ", " + door.getFitWidth());
                door.setScaleX((double)90/225);
                door.setScaleY((double)160/400);
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

    // 도구 핸들러
    public void handleTools(String name) {
        currentTool = tools.get(name);
        showNotification("현재 도구는 \'" + name + "\' 입니다");
        View.setCursor(new ImageCursor(new Image(String.valueOf(getClass().getResource(currentTool.getToolFile())))));
    }

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
        notification.getItems().add("System : " + message);
        notification.scrollTo(notification.getItems().size());
    }

    // FXML문서가 처음으로 Load되었을 때, 초기화 해주는 메소드.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        doorHealth.setProgress(1.0);

        tools = new LinkedHashMap<>();

        tools.put("손", new Tools(30.0, "fist.png", "손"));
        tools.put("신발", new Tools(50.0, "shoes.png", "신발"));
        tools.put("바비인형", new Tools(70.0, "bobby.png", "바비인형"));
        tools.put("깔리", new Tools(300.0, "curry.png", "깔리"));
        tools.put("망치", new Tools(700.0, "hammer.png", "망치"));
		tools.put("욱재 전광판", new Tools(3000.0, "light.png", "욱재 전광판"));
        tools.put("수학의 정석", new Tools(7000.0, "math.jpg", "수학의 정석"));
        tools.put("반달 돌칼", new Tools(10000.0, "halfmoon.png", "반달 돌칼"));
        tools.put("벤젠", new Tools(50000.0, "benzene.png", "벤젠"));
        tools.put("JMS의 손짓", new Tools(100000.0, "teacher.jpg", "JMS의 손짓"));


        setListView(tools);

		// 초기 문 설정
		doorOrder.add(new Doors(3000, "wood.jpg", "나무문"));
		doorOrder.add(new Doors(7000, "glass.jpg", "유리문"));
		doorOrder.add(new Doors(10000, "stone.jpg", "돌문"));
		doorOrder.add(new Doors(50000, "steel.png", "철문"));
		doorOrder.add(new Doors(100000, "stomach.jpg", "명치"));
		doorOrder.add(new Doors(700000, "diamond.jpg", "다이아문"));
		doorOrder.add(new Doors(1000000, "secretary.jpg", "야외점호의 문"));
		doorOrder.add(new Doors(7777777, "heart.jpeg", "심쿵"));

		iterator = doorOrder.iterator();
		currentDoor = iterator.next();
        doorName.setText(currentDoor.getDoorName());
        handleTools("손");

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
                handleTools(tool.getToolName());
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

	//문 생성 시 애니메이션
	public void spawnDoorAnim(ImageView door) {
		ScaleTransition st = new ScaleTransition(Duration.millis(500), door);
		st.setToX((double)225/90);
		st.setToY((double)400/160);
		st.play();
	}

}
