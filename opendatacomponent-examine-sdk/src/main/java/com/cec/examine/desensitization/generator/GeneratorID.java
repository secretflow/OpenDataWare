package com.cec.examine.desensitization.generator;

public class GeneratorID extends Generator {

    private String [] heads = {"110","120","130","140","150","210","220","230","310",
            "320","330","340","350","360","370","410","420","430","440","450","460",
            "500","510","520","530","540","610","620","630","640","650","830","810","820"};
    private String [] month = {"01","02","03","04","05","06","07","08","09","10","11","12"};
    private String [] day = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28"};
    @Override
    public String generateFixed(String seed) {
        String HEAD = this.generateFixedEnumWord(seed, heads);
        String CITY = this.generateFixedLengthNumber(seed, 3);
        String YEAR = this.generateFixedRangeNumber(seed, 1950, 2022);
        String MON = this.generateFixedEnumWord(seed, month);
        String DAY = this.generateFixedEnumWord(seed, month);
        String TAIL = this.generateFixedLengthNumber(seed, 4);

        return HEAD + CITY + YEAR + MON + DAY + TAIL;
    }

}
