package com.example.clay;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static Map<String, Book> bookMap;

    enum Book {

        Genesis("Genesis", 1),
        Exodus("Exodus", 2),
        Leviticus("Leviticus", 3),
        Numbers("Numbers", 4),
        Deuteronomy("Deuteronomy", 5),
        Joshua("Joshua", 6),
        Judges("Judges", 7),
        Ruth("Ruth", 8),
        FirstSamuel("1 Samuel", 9),
        SecondSamuel("2 Samuel", 10),
        FirstKings("1 Kings", 11),
        SecondKings("2 Kings", 12),
        FirstChronicles("1 Chronicles", 13),
        SecondChronicles("2 Chronicles", 14),
        Ezra("Ezra", 15),
        Nehemiah("Nehemiah", 16),
        Esther("Esther", 17),
        Job("Job", 18),
        Psalms("Psalms", 19),
        Proverbs("Proverbs", 20),
        Ecclesiastes("Ecclesiastes", 21),
        SongOfSolomon("Song of Solomon", 22),
        Isaiah("Isaiah", 23),
        Jeremiah("Jeremiah", 24),
        Lamentations("Lamentations", 25),
        Ezekiel("Ezekiel", 26),
        Daniel("Daniel", 27),
        Hosea("Hosea", 28),
        Joel("Joel", 29),
        Amos("Amos", 30),
        Obadiah("Obadiah", 31),
        Jonah("Jonah", 32),
        Micah("Micah", 33),
        Nahum("Nahum", 34),
        Habakkuk("Habakkuk", 35),
        Zephaniah("Zephaniah", 36),
        Haggai("Haggai", 37),
        Zechariah("Zechariah", 38),
        Malachi("Malachi", 39),
        Matthew("Matthew", 40),
        Mark("Mark", 41),
        Luke("Luke", 42),
        John("John", 43),
        Acts("Acts", 44),
        Romans("Romans", 45),
        FirstCorinthians("1 Corinthians", 46),
        SecondCorinthians("2 Corinthians", 47),
        Galatians("Galatians", 48),
        Ephesians("Ephesians", 49),
        Philippians("Philippians", 50),
        Colossians("Colossians", 51),
        FirstThessalonians("1 Thessalonians", 52),
        SecondThessalonians("2 Thessalonians", 53),
        FirstTimothy("1 Timothy", 54),
        SecondTimothy("2 Timothy", 55),
        Titus("Titus", 56),
        Philemon("Philemon", 57),
        Hebrews("Hebrews", 58),
        James("James", 59),
        FirstPeter("1 Peter", 60),
        SecondPeter("2 Peter", 61),
        FirstJohn("1 John", 62),
        SecondJohn("2 John", 63),
        ThirdJohn("3 John", 64),
        Jude("Jude", 65),
        Revelation("Revelation", 66);

        private final String bookName;
        private final int bookNumber;

        Book(String bookName, int bookNumber) {
            this.bookName = bookName;
            this.bookNumber = bookNumber;
        }

        public String getBookName() {
            return bookName;
        }

        public int getBookNumber() {
            return bookNumber;
        }
    }

    public static void createSearchList() {
        bookMap = new HashMap<>();
        for (Book book : Book.values()) {
            String key = book.bookName.toLowerCase();
            // Handle Judges and Jude
            if (book.bookNumber == 7 || book.bookNumber == 65) {
                bookMap.put("jude", Book.Jude);
                bookMap.put("judg", Book.Judges);
                bookMap.put("judge", Book.Judges);
                bookMap.put("judges", Book.Judges);
            }
            // Handle Philippians and Philemon
            else if (book.bookNumber == 50 || book.bookNumber == 57) {
                for (int i = key.length(); i > 4; i--) {
                    bookMap.put(key.substring(0, i), book);
                }
                bookMap.put("phi", Book.Philippians);
                bookMap.put("phil", Book.Philippians);
            }
            // Handles book names that start with number
            else if (book.bookNumber >= 9 && book.bookNumber <= 14
                    || book.bookNumber >= 46 && book.bookNumber <= 47
                    || book.bookNumber >= 52 && book.bookNumber <= 55
                    || book.bookNumber >= 60 && book.bookNumber <= 64)
            {
                for (int i = key.length(); i >= 5; i--) {
                    bookMap.put(key.substring(0, i), book);
                }
            }
            // Handle Job
            else if (book.bookNumber == 18) {
                bookMap.put("job", Book.Job);
            }
            // Others
            else {
                for (int i = key.length(); i >= 3; i--) {
                    bookMap.put(key.substring(0, i), book);
                }
            }
        }
    }

    public static String[] getInputArray(String input) {
        return input.trim().toLowerCase().split(" ");
    }

    public static boolean isValidInputChapter(String[] inputArray) {
        if (inputArray == null) {
            return false;
        }
        if (!(inputArray.length == 2 || inputArray.length == 3)) {
            return false;
        }
        if (inputArray[0] == null || inputArray[1] == null) {
            return false;
        }
        return true;
    }

    public static boolean isValidInputVerse(String[] inputArray) {
        if (inputArray == null) {
            return false;
        }
        if (!(inputArray.length == 2 || inputArray.length == 3)) {
            return false;
        }
        if (inputArray[0] == null || inputArray[1] == null) {
            return false;
        }
        if (inputArray.length == 2) {
            String[] chapterAndVerse = inputArray[1].split(":");
            if (chapterAndVerse.length != 2) {
                return false;
            } else {
                if (!isNumeric(chapterAndVerse[0])) {
                    return false;
                }
                String[] verses = chapterAndVerse[1].split("-");
                if (verses.length != 2) {
                    return isNumeric(chapterAndVerse[1]);
                } else {
                    return isNumeric(verses[0]) && isNumeric(verses[1]);
                }
            }
        } else {
            String[] chapterAndVerse = inputArray[2].split(":");
            if (chapterAndVerse.length != 2) {
                return false;
            } else {
                if (!isNumeric(chapterAndVerse[0])) {
                    return false;
                }
                String[] verses = chapterAndVerse[1].split("-");
                if (verses.length != 2) {
                    return isNumeric(chapterAndVerse[1]);
                } else {
                    return isNumeric(verses[0]) && isNumeric(verses[1]);
                }
            }
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static String[] getSearchKeyWords(String[] inputArray) {
        // length can be 3 or 4
        // {"Luke", "3", "2"} for Luke 3:2
        // {"Luke", "3", "2", "4"} for Luke 3:2-4
        String[] keyWords;
        String bookName;
        String[] chapterAndVerse;
        if (inputArray.length == 2) {
            bookName = inputArray[0];
            chapterAndVerse = inputArray[1].split(":");
        } else {  // For book names starting with number, e.g. 1 Peter
            bookName = inputArray[0] + " " + inputArray[1];
            chapterAndVerse = inputArray[2].split(":");
        }
        String[] verses = chapterAndVerse[1].split("-");
        if (verses.length != 2) {  // single verse insertion
            keyWords = new String[3];
            keyWords[0] = bookName;  // book name
            keyWords[1] = chapterAndVerse[0];  // chapter number
            keyWords[2] = chapterAndVerse[1];  // verse number
        } else {
            keyWords = new String[4];
            keyWords[0] = bookName;  // book name
            keyWords[1] = chapterAndVerse[0];  // chapter number
            keyWords[2] = verses[0];  // starting verse
            keyWords[3] = verses[1];  // ending verse
        }
        return keyWords;
    }
}
