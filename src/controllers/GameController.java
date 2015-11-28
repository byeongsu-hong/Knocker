package controllers;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
import javafx.scene.text.Font;
import javafx.util.Duration;
import main.App;

import java.lang.String;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;

/**
 *
 * Created by frostornge on 15. 11. 24.
 */
public class GameController implements Initializable{

    /** FXML resources **/
    @FXML private AnchorPane View;					// 전체 화면 컨테이너
    @FXML private ProgressBar doorHealth;			// 문 체력바
    @FXML private Button toolsBtn;					// 우측에 위치한 "Tools"버튼
    @FXML private ListView<HBox> toolsPanel;		// 사이드바
    @FXML private Label currentMoney;				// 현재 돈 표시 라벨
    @FXML private Label toolInfo;					// 도구 이름 라벨
    @FXML private Label doorName;					// 문 이름 라벨
    @FXML private ListView<String> notification;	// 알림 메시지 창


    /** Variables **/
    private Doors currentDoor;							// 현재 문 객체를 담는 변수
    private Tools currentTool;							// 현재 도구 객체를 담는 변수

    private Map<String, Tools> tools;					// 도구들을 담고 있는 맵
	private List<Doors> doorOrder = new ArrayList<>();	// 문들을 담고 있는 리스트

	private boolean isTimeToOpen = true;				// 사이드바 토글 구현을 위한 변수

	private ImageView door;								// 문 이미지를 표시해주는 이미지뷰
	private Iterator<Doors> iterator;					// 문을 순서대로 처리하기 위한 iterator

	// FXML문서가 처음으로 Load되었을 때, 초기화 해주는 메소드.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        doorHealth.setProgress(1.0);

		// 초기 도구 설정
        tools = new LinkedHashMap<>();

        tools.put("손", new Tools(30.0, "fist.png", "손"));
        tools.put("신발", new Tools(54.0, "shoes.png", "신발"));
        tools.put("바비인형", new Tools(97.2, "bobby.png", "바비인형"));
        tools.put("깔리", new Tools(174.96, "curry.png", "깔리"));
        tools.put("망치", new Tools(314.928, "hammer.png", "망치"));
        tools.put("욱재 전광판", new Tools(566.8704, "light.png", "욱재 전광판"));
        tools.put("수학의 정석", new Tools(1020.36672, "math.jpg", "수학의 정석"));
        tools.put("반달 돌칼", new Tools(1836.660096, "halfmoon.png", "반달 돌칼"));
        tools.put("벤젠", new Tools(3305.9881728, "benzene.png", "벤젠"));

		// 현존 최강 무기
        tools.put("JMS의 손짓", new Tools(5950.77871104, "teacher.jpg", "JMS의 손짓"));

		// ListView에 도구 정보 등록
        setListView(tools);

        // 초기 문 설정
        doorOrder.add(new Doors(1260.0, "wood.jpg", "나무문"));
        doorOrder.add(new Doors(2772.0, "glass.jpg", "유리문"));
        doorOrder.add(new Doors(6098.4, "stone.jpg", "돌문"));
        doorOrder.add(new Doors(13416.48, "steel.png", "철문"));
        doorOrder.add(new Doors(29516.256, "stomach.jpg", "명치"));
        doorOrder.add(new Doors(64935.7632, "diamond.jpg", "다이아문"));
        doorOrder.add(new Doors(142858.67904, "secretary.jpg", "야외점호의 문"));

        // 지금까지의 최종보스
        doorOrder.add(new Doors(1571445.46944, "heart.jpeg", "심쿵"));

		// currentDoor 변수에 첫번째 문 객체를 담음
        iterator = doorOrder.iterator();
        if(iterator.hasNext()) {
			currentDoor = iterator.next();
			doorName.setText(currentDoor.getDoorName());
		}

		// 시작 도구를 손으로 설정
		handleTools("손");

		// 문 이미지뷰 설정
        door = new ImageView();
        door.setFitWidth(225);
        door.setFitHeight(400);
        door.setLayoutX(296);
        door.setLayoutY(80);
        door.setImage(new Image(String.valueOf(getClass().getResource(currentDoor.getDoorFile()))));
		// 마우스 클릭 이벤트 설정
        door.setOnMouseClicked(event -> handleKnock());

		// door 이미지뷰 객체 뷰에 추가
        View.getChildren().add(door);

		// 윈도우가 focus 상태일 때 커서 재설정
		App.stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (observable.getValue().toString().equals("true")) {
				setCursor();
			}
		});
    }

    // Tools 사이드바 ListView 초기 설정
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


    // 노크를 했을 때 처리하는 메소드
    public void handleKnock() {
		// 문의 체력을 깎는다
        currentDoor.addDamage(currentTool.getPower());
        doorHealth.setProgress(currentDoor.getCurrentHealth() / currentDoor.getHealthFull());

        checkHealth();
    }

    // 문의 체력을 체크하고, 만약 문의 체력이 0 이하일 경우 문을 없앤다.
    private void checkHealth() {

		// 현재 문을 이전 문으로 넘김
		Doors previousDoor = currentDoor;

		// 체력이 없을 경우
        if(doorHealth.getProgress() <= 1.3877787807814457E-16) {
			// 알림창에 메시지를 표시한다
			showNotification("\'" + previousDoor.getDoorName() + "\'이/가 파괴되었다!");
			//기존 문을 제거한다
			View.getChildren().remove(door);

			// 새로운 문 생성
			if(iterator.hasNext()) {
				currentDoor = iterator.next();
                doorName.setText(currentDoor.getDoorName());
                door.setImage(new Image(String.valueOf(getClass().getResource(currentDoor.getDoorFile()))));
                door.setFitWidth(90);
				door.setFitHeight(160);
                door.setLayoutX(355);
                door.setLayoutY(200);
                View.getChildren().add(door);

				// 애니메이션 실행
				spawnDoorAnim(door);
                door.setScaleX((double)90/225);
                door.setScaleY((double)160/400);
            } else {
				Font f = new Font(50);
				Label gameClear = new Label();
				gameClear.setText("이 게임을 정복하셨습니다. ㅂㅂ");
				gameClear.setLayoutX(50);
				gameClear.setLayoutY(100);
				gameClear.setFont(f);

				View.getChildren().removeAll(doorHealth, toolsBtn, toolsPanel, notification, doorName, currentMoney, toolInfo);
				View.getChildren().add(gameClear);
			}

			// 체력 바 재설정
			doorHealth.setProgress(1);
        }
    }

    // 도구 핸들러
    public void handleTools(String name) {
        currentTool = tools.get(name);
		setCursor();
        showNotification("현재 도구는 \'" + name + "\' 입니다");

    }

    // 문이 파괴되었을 때나, 알려줄 것이 있을 때, 알림창을 띄워주는 메소드
    private void showNotification(String message) {
        notification.getItems().add("System : " + message);
        notification.scrollTo(notification.getItems().size());
    }

	// 커서 이미지 변경
	public void setCursor() {
		if(currentTool != null) {
			Image cursorImg = new Image(String.valueOf(getClass().getResource(currentTool.getToolFile())), 70, 0, true, false);
			ImageCursor cursor = new ImageCursor(cursorImg);
			View.setCursor(cursor);
		}
	}

    // 사이드바를 여닫는 메소드
    public void openSideMenu(ActionEvent event) {
        TranslateTransition translateBtn;
        TranslateTransition translatePanel;

        if(isTimeToOpen) {		// 사이드바를 연다.
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
        } else {				// 사이드바를 닫는다.
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


	//문 생성 시 애니메이션
	public void spawnDoorAnim(ImageView door) {
		ScaleTransition st = new ScaleTransition(Duration.millis(500), door);
		st.setFromX((double) 90 / 225);
		st.setFromY((double) 160 / 400);
		st.setToX((double) 225 / 90);
		st.setToY((double) 400 / 160);
		st.play();
	}



}
