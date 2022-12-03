package text;

import font.Symbol;
import imaging.BasicImage;
import imaging.Color;

public class Text {
    private String stringToRender;
    public Text(String stringToRender){
        this.stringToRender = stringToRender;
    }

    public BasicImage getBlackAndWhiteRender(int fontSize, int antialiasFactor, int pixelLetterSpacing, int pixelSpaceSpacing, String fontFolder){
        char[] charSet = new char[stringToRender.length()];
        int charSetLength = 0;
        for (int i = 0; i < stringToRender.length(); i++){
            char currentChar = stringToRender.charAt(i);
            boolean appendToCharList = true;
            for (int j = 0; j < charSetLength; j++){
                if (charSet[j] == currentChar) appendToCharList = false;
            }
            if (currentChar == ' '){
                appendToCharList = false;
            }
            if (appendToCharList){
                charSet[charSetLength] = currentChar;
                charSetLength += 1;
            }
        }
        Symbol[] symbolArray = new Symbol[charSetLength];
        for (int i = 0; i < charSetLength; i++){
            symbolArray[i] = new Symbol("src/font/" + fontFolder + "/" + charSet[i] + ".symbol");
        }
        for (int i = 0; i < symbolArray.length; i++){
            symbolArray[i].getAlphaChannel(fontSize, fontSize, antialiasFactor);
            symbolArray[i].getTrimmedAlphaChannelX();
        }
        int[] symbolWidthArray = new int[charSetLength];
        for (int i = 0; i < symbolWidthArray.length; i++){
            symbolWidthArray[i] = symbolArray[i].getLastAlphaRender()[0].length;
        }
        int imgWidth = 0;
        for (int i = 0; i < stringToRender.length(); i++){
            char currentChar = stringToRender.charAt(i);
            int charIndex = -1;
            for (int j = 0; j < charSetLength; j++){
                if (currentChar == charSet[j]){
                    charIndex = j;
                }
            }
            if (charIndex == -1){
                imgWidth += pixelSpaceSpacing;
            }
            else {
                imgWidth += symbolWidthArray[charIndex];
            }
        }
        imgWidth += stringToRender.length()*pixelLetterSpacing;
        int imgHeight = fontSize;
        BasicImage img = new BasicImage(imgWidth, imgHeight);
        img.setBackground(new Color(255,255,255));
        int currentXLoc = 0;
        for (int i = 0; i < stringToRender.length(); i++){
            char currentChar = stringToRender.charAt(i);
            if (currentChar != ' ') {
                int charIndex = -1;
                for (int j = 0; j < charSetLength; j++) {
                    if (charSet[j] == currentChar){
                        charIndex = j;
                    }
                }
                int[][] alphaArray = symbolArray[charIndex].getLastAlphaRender();
                img.renderAlphaChannelBlackAndWhite(alphaArray, currentXLoc, 0);
                currentXLoc += alphaArray[0].length;
            }
            else {
                currentXLoc += pixelSpaceSpacing;
            }
            currentXLoc += pixelLetterSpacing;
        }
        return img;
    }

    public BasicImage getAlphaRender(int fontSize, int antialiasFactor, int pixelLetterSpacing, int pixelSpaceSpacing, String fontFolder){
        char[] charSet = new char[stringToRender.length()];
        int charSetLength = 0;
        for (int i = 0; i < stringToRender.length(); i++){
            char currentChar = stringToRender.charAt(i);
            boolean appendToCharList = true;
            for (int j = 0; j < charSetLength; j++){
                if (charSet[j] == currentChar) appendToCharList = false;
            }
            if (currentChar == ' '){
                appendToCharList = false;
            }
            if (appendToCharList){
                charSet[charSetLength] = currentChar;
                charSetLength += 1;
            }
        }
        Symbol[] symbolArray = new Symbol[charSetLength];
        for (int i = 0; i < charSetLength; i++){
            symbolArray[i] = new Symbol("src/font/" + fontFolder + "/" + charSet[i] + ".symbol");
        }
        for (int i = 0; i < symbolArray.length; i++){
            symbolArray[i].getAlphaChannel(fontSize, fontSize, antialiasFactor);
            symbolArray[i].getTrimmedAlphaChannelX();
        }
        int[] symbolWidthArray = new int[charSetLength];
        for (int i = 0; i < symbolWidthArray.length; i++){
            symbolWidthArray[i] = symbolArray[i].getLastAlphaRender()[0].length;
        }
        int imgWidth = 0;
        for (int i = 0; i < stringToRender.length(); i++){
            char currentChar = stringToRender.charAt(i);
            int charIndex = -1;
            for (int j = 0; j < charSetLength; j++){
                if (currentChar == charSet[j]){
                    charIndex = j;
                }
            }
            if (charIndex == -1){
                imgWidth += pixelSpaceSpacing;
            }
            else {
                imgWidth += symbolWidthArray[charIndex];
            }
        }
        imgWidth += stringToRender.length()*pixelLetterSpacing;
        int imgHeight = fontSize;
        BasicImage img = new BasicImage(imgWidth, imgHeight);
        int currentXLoc = 0;
        for (int i = 0; i < stringToRender.length(); i++){
            char currentChar = stringToRender.charAt(i);
            if (currentChar != ' ') {
                int charIndex = -1;
                for (int j = 0; j < charSetLength; j++) {
                    if (charSet[j] == currentChar){
                        charIndex = j;
                    }
                }
                int[][] alphaArray = symbolArray[charIndex].getLastAlphaRender();
                img.renderAlphaChannel(alphaArray, currentXLoc, 0, new Color(0,0,0));
                currentXLoc += alphaArray[0].length;
            }
            else {
                currentXLoc += pixelSpaceSpacing;
            }
            currentXLoc += pixelLetterSpacing;
        }
        return img;
    }
}
