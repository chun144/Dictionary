package DictionaryApplication;

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

public class AddDictionary {
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtNewWord, txtType, txtPronoun;
    @FXML
    private TextArea txtExplain;

    public void initialize() {
        setWord();
    }

    public void saveWord() {
        String target = txtNewWord.getText().trim().replaceAll("\\s+", " ");
        String type = txtType.getText().trim();
        String pronunciation = txtPronoun.getText().trim();
        String explain = txtExplain.getText().trim();

        if (target.isEmpty() && explain.isEmpty()) {
            String message = "Please enter the new word and explain it!";
            JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Word word = Dictionary.sw;
        for (Word w : Dictionary.dictionaryList) {
            //Case: edit word
            if (w.getWord_target().equalsIgnoreCase(word.getWord_target())) {
                w.setWord_target(target);
                w.setWord_explain(explain);
                w.setWord_type(type);
                w.setWord_pronunciation(pronunciation);
                JOptionPane.showMessageDialog(null, "Edit word successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                closeWindow();
                return;
            }
        }

        //Case: add word
        Word newWord = new Word(target, explain, type, pronunciation);
        Dictionary.dictionaryList.add(newWord);
        JOptionPane.showMessageDialog(null, "Add new word successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        closeWindow();
    }

    public void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void setWord () {
        if (Dictionary.sw.getWord_target() != null) {
            txtNewWord.setText(Dictionary.sw.getWord_target());
        }
        if (Dictionary.sw.getWord_explain() != null) {
            txtExplain.setText(Dictionary.sw.getWord_explain());
        }
        if (Dictionary.sw.getWord_type() != null) {
            txtType.setText(Dictionary.sw.getWord_type());
        }
        if (Dictionary.sw.getWord_pronunciation() != null) {
            txtPronoun.setText(Dictionary.sw.getWord_pronunciation());
        }
    }
}
