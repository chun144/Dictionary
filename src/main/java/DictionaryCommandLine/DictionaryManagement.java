package DictionaryCommandLine;

import java.io.*;
import java.util.Scanner;
import java.util.List;
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

    public void speech(String text) {
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice syntheticVoice = voiceManager.getVoice("kevin16");
        syntheticVoice.allocate();
        syntheticVoice.speak(text);
        syntheticVoice.deallocate();
    }
}
