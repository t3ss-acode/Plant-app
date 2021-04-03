package com.example.plant_app;

public class InputChecker {

    /**
     * Checks if the given string is empty or not
     *
     * @param name
     * @return true if it is empty
     */
    public boolean isEmpty(String name) {
        if (name.matches("")) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the string can be converted to a number and if the number is within range
     *
     * @param nrStr
     * @return returns -1 if wrong else returns number as int
     */
    public int checkNumber(String nrStr) {
        if(!nrStr.matches("")) {
            int nr;
            try {
                nr = Integer.parseInt(nrStr);
                if (nr <= 0)
                    return -1;
                return nr;
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }
}
