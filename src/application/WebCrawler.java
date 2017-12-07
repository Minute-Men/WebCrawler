package application;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;

public class WebCrawler {
	private static final int MAX_PAGES_TO_SEARCH = 10;
	private Set<String> pagesViewed = new HashSet<String>();
	private List<String> pagesToView = new LinkedList<String>();
	private List<String> successList = new LinkedList<String>();

	public void search(String url, String keyWord) {
		while (this.pagesViewed.size() < MAX_PAGES_TO_SEARCH) {
			String currentUrl;
			Crawler leg = new Crawler();
			if (this.pagesToView.isEmpty()) {
				currentUrl = url;
				this.pagesViewed.add(url);
			} else {
				currentUrl = this.nextUrl();
			}
			leg.crawl(currentUrl);
			boolean success = leg.searchForWord(keyWord);
			if (success) {
				this.successList.add(currentUrl);
			}

			this.pagesToView.addAll(leg.getLinks());
		}
		for (int i = 0; i < successList.size(); i++) {
			if (successList.size() > 0)
				System.out.println("\nPage #" + (i + 1) + " " + successList.get(i));
			else
				System.out.println("No Matches Found.");
		}
		// String results = String.join(", ", successList);
		System.out.println("\nDone Visited " + this.pagesViewed.size() + " web page(s)");
	}

	private String nextUrl() {
		String nextUrl;
		do {
			nextUrl = this.pagesToView.remove(0);
		} while (this.pagesViewed.contains(nextUrl));
		this.pagesViewed.add(nextUrl);
		return nextUrl;
	}

	@FXML
	private Button runButton;
	@FXML
	private Button emailButton;
	@FXML
	private Button removeButton;
	@FXML
	private Button ignoreButton;
	@FXML
	private TextArea keyWordsTextArea;
	@FXML
	private TextArea websitesTextArea;
	@FXML
	private TextArea resultsTextArea;
	@FXML
	private Label resultsLabel;
	@FXML
	public TextArea ignoredTextArea;

	@FXML
	public void initialize() {
		keyWordsTextArea.setText("Trump\nHillary\nNorth Korea\nsoccer\nGaming");
		websitesTextArea.setText("https://www.cnn.com\nhttps://www.foxnews.com\nhttp://www.espn.com\nhttps://pcpartpicker.com/");
	}

	@FXML
	public void clickedRun(ActionEvent e) {
		resultsTextArea.clear();
		String[] websiteArray = websitesTextArea.getText().split("\n");
		String[] keyWordsArray = keyWordsTextArea.getText().split("\n");

		for (int i = 0; i < keyWordsArray.length; i++) {
			for (int j = 0; j < websiteArray.length; j++) {
				searchCrawler(websiteArray[j], keyWordsArray[i]);
			}
		}
	}

	private void searchCrawler(String website, String keyWord) {
		// Starts a thread
		Thread t1 = new Thread(() -> {

			WebCrawler spider = new WebCrawler();
			spider.search(website, keyWord);

			// Goes to main thread to set the text
			Platform.runLater(() -> {

				String results = String.join("\n", spider.successList);

				if (spider.successList.size() == 0) {
					resultsTextArea.appendText("Did not find " + keyWord + " in \n" + website + "\n\n");
				} else {
					resultsTextArea.appendText("Found " + keyWord + " in \n" + results + "\n\n");
				}

			});

		});
		t1.start();
	}


	@FXML
	public void clickedEmail(ActionEvent e) {
	}

	@FXML
	public void clickedRemove(ActionEvent e) {
		keyWordsTextArea.setText(keyWordsTextArea.getText().replace(keyWordsTextArea.getSelectedText(), ""));
	}

	@FXML
	public void clickedIgnore(ActionEvent e) {
		String link = resultsTextArea.getSelectedText();
		resultsTextArea.setText(resultsTextArea.getText().replace(link, ""));
		ignoredTextArea.appendText(link + "\n");
	}

    public void clickedSort(ActionEvent actionEvent) {
	    String[] lines = resultsTextArea.getText().split("\n");
        Arrays.sort(lines);
        resultsTextArea.clear();

        for(int i = 0; i < lines.length; i++){
            resultsTextArea.appendText(lines[i]);
            resultsTextArea.appendText("\n");
        }
    }

}
