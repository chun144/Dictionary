package DictionaryApplication;

import DictionaryCommandLine.Translator;
import DictionaryCommandLine.Word;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.IOException;

public class Dictionary {
    public static List<Word> dictionaryList = new ArrayList<>();
    private final String path = "./data/Dictionary.txt";
    private File file = new File(path);
    public static Word sw = new Word();

    @FXML
    private TextField txtSearch;
    @FXML
    private ListView wordList;
    @FXML
    private TextArea result;
    private Translator translator = new Translator();

    public void initialize() {
        getData();
    }

    private void getData() {
        try {
            dictionaryList.removeAll(dictionaryList);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                try {
                    Word word;
                    String[] splitData = scanner.nextLine().split("\t");
                    String target = splitData[0].trim();
                    String[] splitExplainData = splitData[1].trim().split("/");
                    if (splitExplainData.length == 3) {
                        String type = splitExplainData[0].trim();
                        String pronunciation = splitExplainData[1].trim();
                        String explain = splitExplainData[2].trim();
                        word = new Word(target, explain, type, pronunciation);
                    } else if (splitExplainData.length == 2) {
                        String type = splitExplainData[0].trim();
                        String pronunciation = splitExplainData[1].trim();
                        word = new Word(target, type, pronunciation);
                    } else {
                        String[] anotherSplitData = splitExplainData[0].trim().split(" ");
                        String type = anotherSplitData[0].trim();
                        String explain = anotherSplitData[1].trim();
                        word = new Word();
                        word.setWord_target(target);
                        word.setWord_explain(explain);
                        word.setWord_type(type);
                    }
                    dictionaryList.add(word);
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }

        ObservableList<String> list = FXCollections.observableArrayList();
        for (Word w : dictionaryList) {
            list.add(w.getWord_target());
        }

        wordList.setItems(list);
    }

    public void searchWord(ActionEvent event) {
        String searchKey = txtSearch.getText().trim();
        if (searchKey.isEmpty()) {
            getData();
            return;
        }

        ObservableList<String> list = FXCollections.observableArrayList();
        for (Word word: dictionaryList) {
            if (word.getWord_target().toLowerCase().startsWith(searchKey.toLowerCase())) {
                list.add(word.getWord_target());
            }
        }

        setDataToListView(list);
    }

    public void openAddWordDialog() {
        try {
            sw = new Word();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddDictionary.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Word");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot load add window!");
        }
    }

    public void openEditWordDialog() {
        try {
            if (wordList.getSelectionModel().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a word to edit!");
                return;
            }
            String selectedWord = (String)wordList.getSelectionModel().getSelectedItem();
            for (Word w : dictionaryList) {
                if (w.getWord_target().equalsIgnoreCase(selectedWord)) {
                    sw = new Word(w.getWord_target(), w.getWord_explain(), w.getWord_type(), w.getWord_pronunciation());
                    break;
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddDictionary.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Edit New Word");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot load add window!");
            e.printStackTrace();
        }
    }

    public void deleteWord() {
        if (wordList.getSelectionModel().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a word to delete!");
            return;
        }

        String selectedWord = (String)wordList.getSelectionModel().getSelectedItem();
        String message = "Are you sure you want to delete \"" + selectedWord  + "\" word?";
        int dialogResult = JOptionPane.showConfirmDialog (null, message,"Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            for (Word w : dictionaryList) {
                if (w.getWord_target().equalsIgnoreCase(selectedWord)) {
                    dictionaryList.remove(w);
                    reloadData();
                    JOptionPane.showMessageDialog(null, "Delete word successfully!");
                    return;
                }
            }
        }
    }

    public void explainWord() {
        String target = (String) wordList.getSelectionModel().getSelectedItem();

        for (Word word : dictionaryList) {
            if (word.getWord_target().equalsIgnoreCase(target)) {
                StringBuilder display = new StringBuilder();
                display.append(word.getWord_target() + "\t" + "(" + word.getWord_type() + ")\n");
                if (word.getWord_pronunciation() != null && !word.getWord_pronunciation().isEmpty()) {
                    display.append("/" + word.getWord_pronunciation() + "/\n\n");
                } else {
                    display.append("\n");
                }
                display.append(word.getWord_explain());
                result.setText(display.toString());
                break;
            }
        }
    }

    public void reloadData() {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (Word word: dictionaryList) {
            list.add(word.getWord_target());
        }

        setDataToListView(list);
        result.setText("");
    }

    public void setDataToListView(ObservableList<String> list) {
        wordList.setItems(list.sorted());
    }

    public void dictionaryExportToFile2(ActionEvent event) {
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
            for (Word word : dictionaryList) {
                try {
                    if (word.getWord_type() == null) {
                        word.setWord_type(" ");
                    }
                    if (word.getWord_pronunciation() == null) {
                        word.setWord_pronunciation(" ");
                    }
                    if (word.getWord_explain() == null) {
                        word.setWord_explain(" ");
                    }
                    String dictionary = word.getWord_target() + "\t" + word.getWord_type() + " /" + word.getWord_pronunciation() + "/ " + word.getWord_explain() + "\n";
                    writer.write(dictionary);
                } catch (Exception e) {
                    continue;
                }
            }
            writer.close();
            System.out.println("Export to txt file successfully!");
        } catch (IOException e) {
            System.out.println("Can not find this directory!");
            e.printStackTrace();
        }
    }

    public int binarySearch()
    {
        String w = txtSearch.getText().trim();
        int l = 0;
        int r = dictionaryList.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            int res = w.compareTo(dictionaryList.get(m).getWord_target());
            if (res == 0) {
                return m;
            } else if (res > 0) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return -1;
    }

    public void searchWord2(ActionEvent event) {
        int t = binarySearch();
        if (t != -1) {
            result.setText(dictionaryList.get(t).getWord_target() + " (" + dictionaryList.get(t).getWord_type() + ") \n/"
                    + dictionaryList.get(t).getWord_pronunciation() + "/\n\n" + dictionaryList.get(t).getWord_explain());
        }
    }

    public void searchAPI(ActionEvent event) throws IOException{
        String searchKey = txtSearch.getText().trim();
        if (!searchKey.isEmpty()) {
            String text = "";
            try {
                text = (String) translator.translate("en", "vi", searchKey);
                if (!searchKey.equals(text)) {
                    result.setText(searchKey + "\t" + "\n" + text);
                }
            } catch (Exception e) {
                result.setText("Xin Lỗi!\nKhông tìm thấy từ này. Xin bạn kiểm tra lại chính tả.");
            }
        }
    }
}
