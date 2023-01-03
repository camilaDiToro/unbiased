//package ar.edu.itba.paw.webapp.model;
//
//import ar.edu.itba.paw.model.news.TextType;
//import org.springframework.web.servlet.ModelAndView;
//
//
//public class MyModelAndView extends ModelAndView {
//
//    private MyModelAndView(String viewName, String pageTitle, TextType textType) {
//        super(viewName);
//        addObject("pageTitle", pageTitle);
//        addObject("textType", textType);
//    }
//
//    public static class Builder {
//        private final MyModelAndView mav;
//        private final TextType textType;
//        private final StringBuilder params = new StringBuilder();
//        public Builder(String viewName, String pageTitle, TextType textType) {
//            mav = new MyModelAndView(viewName, pageTitle, textType);
//            this.textType = textType;
//        }
//
//        public MyModelAndView.Builder withStringParam(String param) {
//            if (TextType.INTERCODE != textType){
//                throw new IllegalStateException();
//            }
//
//            this.params.append(',').append(param);
//            return this;
//        }
//
//        public MyModelAndView.Builder withObject(String name, Object o) {
//            mav.addObject(name, o);
//            return this;
//        }
//
//        public MyModelAndView.Builder withObject(Object o) {
//            mav.addObject(o);
//            return this;
//        }
//
//        public ModelAndView build() {
//            mav.addObject("stringParams", params.toString());
//            return mav;
//        }
//    }
//}
