package Client.Utils;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuManager {

	private Stage stage;
	private Table table;
	private Skin skin;
	private BitmapFont font;
	
	private LinkedList<TextButton> buttons;
	
	private boolean fillCell;
	
	private float cellWidth;
	private float cellHeight;
	private float padding;
	
	
	public MenuManager(Viewport view) {
		
		stage = new Stage(view);
		Gdx.input.setInputProcessor(stage);
		skin = createBasicSkin();
		
		buttons = new LinkedList<TextButton>();
		
		fillCell = true;
		
		cellWidth = 2;
		cellHeight = 2;
		
		padding = 5;
		
		table = new Table();
		
		table.setFillParent(true);
		stage.addActor(table);
		
	}
	
	
	
	public void render(float delta){
		stage.act();
		stage.draw();
	}
	
	public TextField addTextField(){
		
		TextField field = new TextField("", skin);
		table.add(field).pad(padding).width(cellWidth).height(cellHeight);
		return field;
		
	}
	
	public void setFillCell(boolean enable){
		fillCell = enable;
	}
	
	public void setFontSize(int size){
		skin = createBasicSkin();
	}
	
	public void setActorCellSize(float width, float height, Actor actor){
		Cell cell = table.getCell(actor);
		cell.width(width);
		cell.height(height);
	}
	
	public void setCellSize(float width, float height){
		cellWidth = width;
		cellHeight = height;
	}
	
	public void setCellPadding(float padding){
		this.padding = padding;
	}
	
	public void dispose(){
		skin.dispose();
		stage.dispose();
	}
	
	
	public TextButton addTextButton(String text){
		
		TextButton button = new TextButton(text, skin);
		
		if(fillCell){
			table.add(button).pad(padding).width(cellWidth).height(cellHeight);
		}else{
			table.add(button).width(cellWidth).height(cellHeight).pad(padding).getActor();
		}
		
		if(buttons.size() == 0){
			button.setColor(Color.DARK_GRAY);
		}
		
		buttons.add(button);
		
		return button;
	}
	
	public Label addLabel(String text){
		Label label = new Label(text, skin);
		table.add(label).pad(padding).width(cellWidth).height(cellHeight);
		return label;
	}
	
	public void row(){
		table.row();
	}
	
	private Skin createBasicSkin(){
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Constants.FONT_FILE);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 32;
		font = generator.generateFont(parameter);
		generator.dispose();
		
		Skin skin = new Skin();
		skin.add("default", font);
		
		Pixmap pixmap = new Pixmap((int)Constants.V_WIDTH/4, (int)Constants.V_HEIGHT/10, Pixmap.Format.RGB888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("background", new Texture(pixmap));
		
		TextButton.TextButtonStyle textStyle = new TextButton.TextButtonStyle();
		textStyle.up = skin.newDrawable("background", Color.GRAY);
		textStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
		textStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
		textStyle.font = skin.getFont("default");
		skin.add("default", textStyle);
		
		Label.LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);
		skin.add("default", labelStyle);
		
		TextField.TextFieldStyle fieldStyle = new TextFieldStyle();
		fieldStyle.font = font;
		fieldStyle.fontColor = Color.WHITE;
		fieldStyle.background = skin.newDrawable("background", Color.LIGHT_GRAY);
		skin.add("default", fieldStyle);
		
		return skin;
	}

	
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}
	
}