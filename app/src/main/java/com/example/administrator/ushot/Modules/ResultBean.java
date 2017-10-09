package com.example.administrator.ushot.Modules;

import java.util.List;

/**
 * Created by Administrator on 2017/10/9 0009.
 */

public class ResultBean {

    private SceneBean Scene;
    private AnalysisBean Analysis;
    public SceneBean getScene() {
        return Scene;
    }

    public void setScene(SceneBean Scene) {
        this.Scene = Scene;
    }

    public AnalysisBean getAnalysis() {
        return Analysis;
    }

    public void setAnalysis(AnalysisBean Analysis) {
        this.Analysis = Analysis;
    }

    public static class SceneBean {
        /**
         * Category : home_theater
         * Attributes : ["no horizon","man-made","enclosed area","cloth","vertical components"]
         * Indoor : True
         * score : 0.477065652609
         */

        private String Category;
        private String Indoor;
        private String score;
        private List<String> Attributes;

        public String getCategory() {
            return Category;
        }

        public void setCategory(String Category) {
            this.Category = Category;
        }

        public String getIndoor() {
            return Indoor;
        }

        public void setIndoor(String Indoor) {
            this.Indoor = Indoor;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public List<String> getAttributes() {
            return Attributes;
        }

        public void setAttributes(List<String> Attributes) {
            this.Attributes = Attributes;
        }
    }

    public static class AnalysisBean {
        /**
         * BalancingElement : 0.0409112684429
         * Symmetry : 0.132171437144
         * RuleOfThirds : -0.289971619844
         * Light : -0.241546034813
         * MotionBlur : -0.0573715120554
         * DoF : -0.199838683009
         * ColorHarmony : -0.00141979753971
         * Content : -0.199531957507
         * Object : -0.0151377618313
         * score : 35.1863801479
         * VividColor : 0.0811899900436
         * Repetition : 0.16214543581
         */

        private String BalancingElement;
        private String Symmetry;
        private String RuleOfThirds;
        private String Light;
        private String MotionBlur;
        private String DoF;
        private String ColorHarmony;
        private String Content;
        private String Object;
        private String score;
        private String VividColor;
        private String Repetition;

        public String getBalancingElement() {
            return BalancingElement;
        }

        public void setBalancingElement(String BalancingElement) {
            this.BalancingElement = BalancingElement;
        }

        public String getSymmetry() {
            return Symmetry;
        }

        public void setSymmetry(String Symmetry) {
            this.Symmetry = Symmetry;
        }

        public String getRuleOfThirds() {
            return RuleOfThirds;
        }

        public void setRuleOfThirds(String RuleOfThirds) {
            this.RuleOfThirds = RuleOfThirds;
        }

        public String getLight() {
            return Light;
        }

        public void setLight(String Light) {
            this.Light = Light;
        }

        public String getMotionBlur() {
            return MotionBlur;
        }

        public void setMotionBlur(String MotionBlur) {
            this.MotionBlur = MotionBlur;
        }

        public String getDoF() {
            return DoF;
        }

        public void setDoF(String DoF) {
            this.DoF = DoF;
        }

        public String getColorHarmony() {
            return ColorHarmony;
        }

        public void setColorHarmony(String ColorHarmony) {
            this.ColorHarmony = ColorHarmony;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getObject() {
            return Object;
        }

        public void setObject(String Object) {
            this.Object = Object;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getVividColor() {
            return VividColor;
        }

        public void setVividColor(String VividColor) {
            this.VividColor = VividColor;
        }

        public String getRepetition() {
            return Repetition;
        }

        public void setRepetition(String Repetition) {
            this.Repetition = Repetition;
        }
    }
}
