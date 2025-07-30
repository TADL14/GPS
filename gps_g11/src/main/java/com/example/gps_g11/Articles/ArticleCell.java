package com.example.gps_g11.Articles;

import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ArticleCell extends ListCell<ArticleItem> {
    @Override
    protected void updateItem(ArticleItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            Text titleText = new Text(item.getTitle());
            titleText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;-fx-font-family: 'Arial';");

            Text linkText = new Text(item.getLink());
            linkText.setStyle("-fx-font-size: 12px; -fx-fill: blue; -fx-underline: true;");
            linkText.setOnMouseClicked(event -> openLinkInBrowser(item.getLink()));

            VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.getChildren().addAll(new TextFlow(titleText), new TextFlow(linkText));

            setGraphic(vbox);
        }
    }

    private void openLinkInBrowser(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
