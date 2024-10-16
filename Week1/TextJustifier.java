public class TextJustifier {
    
    public static int[] arrayWithCountOfLines(String[] words, int maxWidth){
        int currInd=0;
        int count=1;
        int[] countOfWordInEveryLine = new int[words.length];
        for(int i = 0;i < words.length;i++){
            currInd+=words[i].length();
            if(currInd>maxWidth){
                count++;
                currInd=words[i].length();
            }
            countOfWordInEveryLine[count-1]++;
            if(currInd!=maxWidth-1){
                currInd++;
            }
        }
        return countOfWordInEveryLine;
    }
    
    public static int countOfLines(int[] arr){
        int count=0;
        for(int i = 0;i < arr.length;i++){
            if(arr[i]==0){
                return count;
            }
            count++;
        }
        return count;
    }
    
    public static String putWordsInLine(String[] words, int maxWidth, int countOfWords, int startIdx, boolean isLastLine) {
        StringBuilder line = new StringBuilder();
        int totalChars = 0;
        for (int i = 0; i < countOfWords; i++) {
            totalChars += words[startIdx + i].length();
        }
        int totalSpaces = maxWidth - totalChars;
        int gaps = countOfWords - 1;
        if (isLastLine || gaps == 0) {
            for (int i = 0; i < countOfWords; i++) {
                line.append(words[startIdx + i]);
                if (i < countOfWords - 1) {
                    line.append(" ");
                }
            }
            while (line.length() < maxWidth) {
                line.append(" ");
            }
        } else {
            int spacesPerGap = totalSpaces / gaps;
            int extraSpaces = totalSpaces % gaps;

            for (int i = 0; i < countOfWords; i++) {
                line.append(words[startIdx + i]);
                if (i < gaps) {
                    int spacesToApply = spacesPerGap + (i < extraSpaces ? 1 : 0);
                    line.append(" ".repeat(spacesToApply));
                }
            }
        }

        return line.toString();
    }
    
    public static String[] justifyText(String[] words, int maxWidth){
        int[] arr = arrayWithCountOfLines(words,maxWidth);
        int countOfLines = countOfLines(arr);
        String[] returnStr = new String[countOfLines];
        int currIndex = 0;
        for(int i = 0;i < countOfLines;i++){
            boolean isLastLine = (i == countOfLines - 1);
            returnStr[i] = putWordsInLine(words, maxWidth, arr[i], currIndex, isLastLine);
            currIndex += arr[i];
        }
        return returnStr;
    }
    
    public static void main(String[] args) {
        String[] str = {"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer."};
        String[] str1 = justifyText(str,20);
        for (String current : str1) {
            System.out.println(current);
        }
    }
}
