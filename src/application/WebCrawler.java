/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

public class WebCrawler {
	private static final int MAX_PAGES_TO_SEARCH = 50;
	public TextArea ignoredTextArea;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();
	private List<String> sucesses = new LinkedList<String>();

	public void search(String url, String searchWord) {
		while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
			String currentUrl;
			Crawler leg = new Crawler();
			if (this.pagesToVisit.isEmpty()) {
				currentUrl = url;
				this.pagesVisited.add(url);
			} else {
				currentUrl = this.nextUrl();
			}
			leg.crawl(currentUrl);
			boolean success = leg.searchForWord(searchWord);
			if (success) {
				this.sucesses.add(currentUrl);
			}

			this.pagesToVisit.addAll(leg.getLinks());
		}
		for (int i = 0; i < sucesses.size(); i++) {
			if (sucesses.size() > 0)
				System.out.println("\nPage #" + (i + 1) + " " + sucesses.get(i));
			else
				System.out.println("No Matches Found.");
		}
		String results = String.join(", ", sucesses);
		System.out.println("\nDone Visited " + this.pagesVisited.size() + " web page(s)");
	}

	private String nextUrl() {
		String nextUrl;
		do {
			nextUrl = this.pagesToVisit.remove(0);
		} while (this.pagesVisited.contains(nextUrl));
		this.pagesVisited.add(nextUrl);
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
	public void clickedRun(ActionEvent e) {

		WebCrawler spider = new WebCrawler();
//		spider.search("http://www.cnn.com/", "Trump");
		spider.search(websitesTextArea.getText(), keyWordsTextArea.getText());
		for (int i = 0; i < sucesses.size(); i++) {
			if (sucesses.size() > 0)
				resultsTextArea.setText(("\nPage# " + (i + 1) + " " + sucesses.get(i)));

			else
				resultsTextArea.setText("No Matches Found.");
		}
	
		
		String results = String.join("\n", spider.sucesses);
		resultsTextArea.setText(results);
	}


	@FXML
	public void clickedEmail(ActionEvent e) {
	}

	@FXML
	public void clickedRemove(ActionEvent e) {
		keyWordsTextArea.setText(keyWordsTextArea.getText().replace(keyWordsTextArea.getSelectedText(),""));
	}

	@FXML
	public void clickedIgnore(ActionEvent e) {
		String link = resultsTextArea.getSelectedText();
		resultsTextArea.setText(resultsTextArea.getText().replace(resultsTextArea.getSelectedText(),""));
		ignoredTextArea.appendText(link);
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
