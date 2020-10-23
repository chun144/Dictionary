package DictionaryCommandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DictionaryCommandLine {
    public void showAllWords() {
        System.out.println("No\t|English\t\t|Vietnamese");
        List<Word> list = Dictionary.DictionaryList;
        int no = 0;
        for(Word word : list) {
            no++;
            System.out.println(no + "\t|" + word.getWord_target() + "\t\t|" + word.getWord_explain());
        }
        System.out.println("");
    }

    public void dictionaryBasic() {
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        dictionaryManagement.insertFromCommandline();
        showAllWords();
    }


    public void dictionaryAdvanced() {
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        dictionaryManagement.insertFromFile();
        showAllWords();
        dictionaryManagement.dictionaryLookup();
        //dictionaryManagement.dictionaryExportToFile();
    }

    public void dictionaryEdited() {
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        dictionaryManagement.editFromCommandline();
        showAllWords();
    }

    public void dictionaryDeleted() {
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        dictionaryManagement.deleteFromCommandline();
        showAllWords();
    }

    public void dictionarySearcher() {
        List<Word> list = Dictionary.DictionaryList;
        String result = "";
        List<String> resultList = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the search word: ");
        String searchKey = sc.nextLine();
        for (Word word : list) {
            if (word.getWord_target().toLowerCase().startsWith(searchKey.toLowerCase())) {
                resultList.add(word.getWord_target());
            }
        }

        if (resultList.size() > 0) {
            result = String.join(", ", resultList);
        } else {
            result = "No match result";
        }

        System.out.println("Search results: " + result);
    }
}
