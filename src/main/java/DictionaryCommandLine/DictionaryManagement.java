package DictionaryCommandLine;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class DictionaryManagement {
    private Scanner scanner;

    public void insertFromCommandline() {
        scanner = new Scanner(System.in);
        System.out.print("Enter the new word: ");
        String newWord = scanner.nextLine();
        System.out.print("Enter the Vietnamese meaning: ");
        String vnMeaning = scanner.nextLine();
        Word word = new Word(newWord, vnMeaning);
        Dictionary.DictionaryList.add(word);
    }

    public void insertFromFile() {
        scanner = new Scanner(System.in);
        System.out.print("Enter file name: ");
        String fileName = scanner.nextLine();
        File file = new File(fileName);
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] splitData = scanner.nextLine().split("\\t");
                Word word = new Word(splitData[0], splitData[1]);
                Dictionary.DictionaryList.add(word);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
    }

    public void dictionaryLookup() {
        DictionaryCommandLine cmd = new DictionaryCommandLine();
        cmd.dictionarySearcher();
    }

    public void editFromCommandline() {
        scanner = new Scanner(System.in);
        System.out.print("Enter the new word to edit: ");
        String newWord = scanner.nextLine();
        List<Word> wordList = Dictionary.DictionaryList;
        for (Word word : wordList) {
            if (newWord.equalsIgnoreCase(word.getWord_target())) {
                System.out.print("Enter the edited word: ");
                String editedWord = scanner.nextLine();
                System.out.print("Enter the edited vietnamese meaning: ");
                String vnMeaning = scanner.nextLine();
                word.setWord_target(editedWord);
                word.setWord_explain(vnMeaning);
                break;
            }
        }
    }

    public void deleteFromCommandline() {
        boolean isExisted = false;
        scanner = new Scanner(System.in);
        System.out.print("Enter the word to delete: ");
        String newWord = scanner.nextLine();
        List<Word> wordList = Dictionary.DictionaryList;
        for (Word word : wordList) {
            if (newWord.equalsIgnoreCase(word.getWord_target())) {
                wordList.remove(word);
                isExisted = true;
                break;
            }
        }

        if (isExisted) {
            System.out.println("Delete " + newWord + " successfully!");
        } else {
            System.out.println("Can not find this word!");
        }
    }

    public void dictionaryExportToFile() {
        try {
            scanner = new Scanner(System.in);
            System.out.print("Enter the path to save directory: ");
            String path = scanner.nextLine();
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
            List<Word> list = Dictionary.DictionaryList;
            for (Word word : list) {
                String dictionary = word.getWord_target() + "\t" + word.getWord_explain() + "\n";
                writer.write(dictionary);
            }
            writer.close();
            System.out.println("Export to txt file successfully!");
        } catch (IOException e) {
            System.out.println("Can not find this directory!");
            e.printStackTrace();
        }
    }

    public String translate(String langFrom, String langTo, String text) throws IOException {
        String urlStr = "https://script.google.com/macros/s/AKfycbxaDvIUInh4y7WCLexn4Ij5J2vKIbHJIAkj7vjKYfvhGizmka0/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public void speech(String text) {
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice syntheticVoice = voiceManager.getVoice("kevin16");
        syntheticVoice.allocate();
        syntheticVoice.speak(text);
        syntheticVoice.deallocate();
    }
}
