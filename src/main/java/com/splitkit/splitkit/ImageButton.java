package com.splitkit.splitkit;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton extends Button{
	public ImageButton(Image image) {  
    	getStylesheets().add(ImageButton.class.getResource("/App.css").toExternalForm());
        setGraphic(new ImageView(image));
        setId("remove-button");
    }
}
