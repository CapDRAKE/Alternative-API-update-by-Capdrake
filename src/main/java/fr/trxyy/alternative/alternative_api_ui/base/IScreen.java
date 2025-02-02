package fr.trxyy.alternative.alternative_api_ui.base;

import java.awt.Desktop;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.utils.Logger;
import fr.trxyy.alternative.alternative_api.utils.Mover;
import fr.trxyy.alternative.alternative_api.utils.ResourceLocation;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * @author Trxyy
 */
public class IScreen {
	/**
	 * The resource location
	 */
	private ResourceLocation RESOURCE_LOCATION = new ResourceLocation();
	/**
	 * The logo Image
	 */
	private ImageView logoImage;
	/**
	 * The position X
	 */
	private int posX = 0;
	/**
	 * The position Y
	 */
	private int posY = 0;

	/**
	 * The Constructor
	 * @param engine The GameEngine instance
	 * @param img The image
	 * @param posX_ The position X
	 * @param posY_ The position Y
	 * @param sizeX The size X
	 * @param sizeY The size Y
	 * @param root The Pane to add
	 * @param animate The enum for animate logo or not
	 */
	public void drawLogo(GameEngine engine, Image img, int posX_, int posY_, int sizeX, int sizeY, Pane root, Mover animate) {
		this.logoImage = new ImageView();
		this.logoImage.setImage(img);
		this.logoImage.setFitWidth(sizeX);
		this.logoImage.setFitHeight(sizeY);
	    this.posX = posX_;
	    this.posY = posY_;
	    this.logoImage.setLayoutX(this.posX);
	    this.logoImage.setLayoutY(this.posY);
		root.getChildren().add(this.logoImage);
		if (animate.equals(Mover.MOVE)) {
			animateLogo();
		}
	}
	
	/**
	 * To animate the logo
	 */
	private void animateLogo() {
		AnimationTimer animate = new AnimationTimer() {
			public void handle(long now) {
				float multplicatorSize = 54.25F;
				int mouseX = (MouseInfo.getPointerInfo().getLocation()).x;
				int mouseY = (MouseInfo.getPointerInfo().getLocation()).y;
				logoImage.setLayoutX((posX - mouseX / multplicatorSize));
				logoImage.setLayoutY((posY - mouseY / multplicatorSize));
			}
		};
		animate.start();
	}

	/**
	 * Draw a background image
	 * @param engine The GameEngine instance
	 * @param root The pane to add
	 * @param img The image name
	 */
	public void drawBackgroundImage(GameEngine engine, Pane root, String img) {
		ImageView backgroundImage = new ImageView();
		backgroundImage.setImage(getResourceLocation().loadImage(engine, img));
		backgroundImage.setFitWidth(engine.getLauncherPreferences().getWidth());
		backgroundImage.setFitHeight(engine.getLauncherPreferences().getHeight());
		backgroundImage.setLayoutX(0);
		backgroundImage.setLayoutY(0);
		root.getChildren().add(backgroundImage);
	}

	/**
	 * Draw a animated background (mp4)
	 * @param engine The GameEngine instance
	 * @param root The Pane to add
	 * @param media The Media name
	 */
	public void drawAnimatedBackground(GameEngine engine, Pane root, String media) {
		MediaPlayer player = new MediaPlayer(getResourceLocation().getMedia(engine, media));
		MediaView viewer = new MediaView(player);
		viewer.setFitWidth(engine.getLauncherPreferences().getWidth());
		viewer.setFitHeight(engine.getLauncherPreferences().getHeight());
		player.setAutoPlay(true);
		viewer.setPreserveRatio(false);
		player.setCycleCount(-1);
		player.play();
		root.getChildren().add(viewer);
	}

	/**
	 * Load a image
	 * @param image The image name
	 * @return The image loaded in Swing and converted to FX
	 */
	public Image loadImage(String image) {
		BufferedImage bufferedImage = null;
		try {
			Logger.log(" " + getResourceLocation() + image);
			bufferedImage = ImageIO.read(IScreen.class.getResourceAsStream(getResourceLocation() + image));
		} catch (IOException e) {
			Logger.log("Echec du chargement de la ressource demandée.");
		}
		Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
		return fxImage;
	}

	/**
	 * Open a link
	 * @param urlString The url to open
	 */
	public void openLink(String urlString) {
		try {
			Desktop.getDesktop().browse(new URL(urlString).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play a sound
	 * @param sound The sound name
	 */
	public void playSound(String sound) {
		URL resourceUrl = IScreen.class.getResource(getResourceLocation() + sound);
		System.out.println(IScreen.class.getResource("" + getResourceLocation()));
		System.out.println(resourceUrl);
		Media theMedia = null;
		try {
			theMedia = new Media(resourceUrl.toURI().toString());
		} catch (URISyntaxException e) {
			Logger.log(e.toString());
		}
		final MediaPlayer mediaPlayer = new MediaPlayer(theMedia);
		mediaPlayer.play();
	}

	/**
	 * @return The resource location
	 */
	public ResourceLocation getResourceLocation() {
		return RESOURCE_LOCATION;
	}

}
