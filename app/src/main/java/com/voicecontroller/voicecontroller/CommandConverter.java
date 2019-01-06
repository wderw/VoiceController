package com.voicecontroller.voicecontroller;

import android.os.Debug;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class CommandConverter {

    public ArrayList<String> turnOnCommands;
    public ArrayList<String> turnOffCommands;
    public ArrayList<String> changeCommands;

    public Map<String, Integer> outputMappings;

    public CommandConverter()
    {
        turnOnCommands = new ArrayList<>();
        turnOffCommands = new ArrayList<>();
        changeCommands = new ArrayList<>();
        outputMappings = new HashMap<>();

        turnOnCommands.add("włącz");

        turnOffCommands.add("wyłącz");

        changeCommands.add("przełącz");
        changeCommands.add("zmień");

        outputMappings.put("pierwszy", 1);
        outputMappings.put("jeden", 1);
        outputMappings.put("1", 1);

        outputMappings.put("drugi", 2);
        outputMappings.put("dwa", 2);
        outputMappings.put("2", 2);

        outputMappings.put("trzeci", 3);
        outputMappings.put("trzy", 3);
        outputMappings.put("3", 3);

        outputMappings.put("czwarty", 4);
        outputMappings.put("cztery", 4);
        outputMappings.put("4", 4);

        outputMappings.put("piąty", 5);
        outputMappings.put("pięć", 5);
        outputMappings.put("5", 5);
    }

    // mocno eksperymentalne
    public static boolean areCloseEnough(String stringA, String stringB, int badCharsAllowed, int maxLengthDifference, boolean matchCase)
    {
        int actualLengthDifference = Math.abs(stringA.length() - stringB.length());
        if(actualLengthDifference > maxLengthDifference)
        {
            return false;
        }

        if(stringA.length() == 1 || stringB.length() == 1)
        {
            // taki hack zeby jak zwroci sama cyferke nie pozwalac na bledy typu "1" to prawie "2"
            badCharsAllowed = 0;
        }

        int actualBadChars = 0;
        String iteratedOver = null;
        if(stringA.length() < stringB.length())
        {
            iteratedOver = stringA;
        }
        else
        {
            iteratedOver = stringB;
        }

        for(int i = 0; i < iteratedOver.length(); i++)
        {
            char letterA = stringA.charAt(i);
            char letterB = stringB.charAt(i);
            if(!matchCase)
            {
                letterA = Character.toLowerCase(stringA.charAt(i));
                letterB = Character.toLowerCase(stringB.charAt(i));
            }

            if(letterA != letterB)
            {
               actualBadChars++;
            }
            if(actualBadChars > badCharsAllowed)
            {
                return false;
            }
        }
        return true;
    }

    public String convert(String voiceCommand) {
        ArrayList<String> voiceCommandTokens = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(voiceCommand);
        while (st.hasMoreTokens()) {
            String nextToken = st.nextToken();
            voiceCommandTokens.add(nextToken);
            Log.d("DEBUG_VOICECONTROLLER", nextToken);
        }

        if(voiceCommandTokens.size() < 2)
        {
            Log.e("ERROR_VOICECALLER", "Command could not be parsed, returning empty string.");
            return "";
        }

        String resultCommand = "";

        for (String cmd : turnOnCommands) {
            if (areCloseEnough(voiceCommandTokens.get(0), cmd, 0 ,0, false)) {
                resultCommand += "ON";
                break;
            }
        }

        for (String cmd : turnOffCommands) {
            if (areCloseEnough(voiceCommandTokens.get(0), cmd, 0 ,0, false)) {
                resultCommand += "OF";
                break;
            }
        }

        for (String cmd : changeCommands) {
            if (areCloseEnough(voiceCommandTokens.get(0), cmd, 0 ,0, false)) {
                resultCommand += "CH";
                break;
            }
        }

        for (Map.Entry<String, Integer> entry : outputMappings.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            if (areCloseEnough(key, voiceCommandTokens.get(1), 1, 0, false)) {
                resultCommand += String.valueOf(value);
                break;
            }
        }

        if(resultCommand.length() != 3)
        {
            Log.e("ERROR_VOICECALLER", "Command could not be parsed, returning empty string.");
            return "";
        }

        return resultCommand;
    }
}
