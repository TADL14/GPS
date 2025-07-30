package com.example.gps_g11.Articles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.URL;

import java.io.IOException;

public class ArticlesControl {

    @FXML
    private Label messageLabel;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView logoImageView;

    @FXML
    private ImageView profileIcon;

    @FXML
    private ListView<ArticleItem> articlesListView;

    @FXML
    public void initialize() {
        logoImageView.setOnMouseEntered(event -> logoImageView.setCursor(javafx.scene.Cursor.HAND));
        logoImageView.setOnMouseExited(event -> logoImageView.setCursor(javafx.scene.Cursor.DEFAULT));

        loadRSSFeed();
    }

    public void gotoHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/Home.fxml"));
            Parent homeRoot = loader.load();
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.setTitle("Home - SproutSmart");
            stage.show();
        } catch (IOException e) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Fail to load Home file.");
        }
    }

    private ObservableList<ArticleItem> allArticles = FXCollections.observableArrayList();

    public void searchArticles() {
        String searchQuery = searchField.getText().toLowerCase(); // Convert to lowercase for case-insensitive search

        // Call a generic filter function
        ObservableList<ArticleItem> filteredArticles = filterArticles(searchQuery);

        // Update the ListView with the filtered articles
        articlesListView.setItems(filteredArticles);

        // Provide feedback if no articles match the search
        if (filteredArticles.isEmpty()) {
            messageLabel.setTextFill(Color.BLUE);
            messageLabel.setText("No articles found for your search query.");
        } else {
            messageLabel.setText("");
        }
    }

    private ObservableList<ArticleItem> filterArticles(String searchQuery) {
        // If the search field is empty, return all articles
        if (searchQuery.isEmpty()) {
            return allArticles;
        }

        // Filter articles based on the search query
        ObservableList<ArticleItem> filteredArticles = FXCollections.observableArrayList();
        for (ArticleItem article : allArticles) {
            if (article.getTitle().toLowerCase().contains(searchQuery)) {
                filteredArticles.add(article);
            }
        }

        return filteredArticles;
    }

    public void loadRSSFeedWithSeach() {
        String feedUrl = "https://mypureplants.com/feed";

        try {
            URL url = new URL(feedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            allArticles.clear();

            for (SyndEntry entry : feed.getEntries()) {
                String articleTitle = entry.getTitle();
                String articleLink = entry.getLink();
                ArticleItem article = new ArticleItem(articleTitle, articleLink);
                allArticles.add(article);
            }

            // Update the ListView with all articles
            searchArticles(); // Automatically applies any active search query
            articlesListView.setCellFactory(listView -> new ArticleCell());

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Failed to load RSS feed.");
        }
    }

    public void loadRSSFeed()
    {
        String feedUrl = "https://mypureplants.com/feed";

        try {
            URL url = new URL(feedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            articlesListView.getItems().clear();

            for (SyndEntry entry : feed.getEntries()) {
                String articleTitle = entry.getTitle();
                String articleLink = entry.getLink();
                articlesListView.getItems().add(new ArticleItem(articleTitle, articleLink));
            }

            articlesListView.setCellFactory(listView -> new ArticleCell());

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Failed to load RSS feed.");
        }
    }
    @FXML
    private void handleProfileIconClick() {
        try {
            // Carregar o FXML da página de perfil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/ProfilePage.fxml"));
            Parent profileRoot = loader.load();

            // Configurar nova cena com o perfil
            Stage stage = (Stage) messageLabel.getScene().getWindow(); // Obtém o Stage atual a partir do messageLabel
            stage.setScene(new Scene(profileRoot));
            stage.setTitle("User Profile - SproutSmart");
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Failed to load Profile page.");
        }
    }
}

/*articlesListView.setOnMouseClicked(event -> {
                    if (!articlesListView.getSelectionModel().isEmpty()) {
                        String selectedArticle = articlesListView.getSelectionModel().getSelectedItem();
                        System.out.println("Selected article: " + selectedArticle);
                        System.out.println("Link: " + link);
                        // Open the link in a browser (optional)
                    }
                });*/
