package application;

import utils.LanguageResource;

public enum FxmlView {
    LOGIN{
        @Override
        String getTitle(){
            return getTitleFromResources("login_title");
        }
        @Override
        String getFxmlFile(){
            return "/gui/login/login.fxml";
        }
    }, USERREGISTER{
        @Override
        String getTitle(){
            return getTitleFromResources("registration_title");
        }
        @Override
        String getFxmlFile(){
            return "/gui/login/userregister/user_register.fxml";
        }
    }, ADMIN{
        @Override
        String getTitle(){
            return getTitleFromResources("main_title");
        }
        @Override
        String getFxmlFile(){
            return "/gui/adminmain/admin_main.fxml";
        }
    }, ADMINCHANGEBOOK{
        @Override
        String getTitle(){
            return getTitleFromResources("change_book_title");
        }
        @Override
        String getFxmlFile(){
            return "/gui/adminchangebook/change_book.fxml";
        }
    }, USER{
        @Override
        String getTitle(){
            return getTitleFromResources("main_title");
        }
        @Override
        String getFxmlFile(){
            return "/gui/usermain/user_main.fxml";
        }
    }, USERCANCELCONFIRM{
        @Override
        String getTitle(){
            return getTitleFromResources("cancel_order_title");
        }
        @Override
        String getFxmlFile(){
            return "/gui/usermain/confirmation/cancel_confirmation.fxml";
        }
    }, USERMAKEORDER{
        @Override
        String getTitle(){
            return getTitleFromResources("complete_order_title");
        }
        @Override
        String getFxmlFile(){
            return "/gui/usermain/makeorder/make_order.fxml";
        }
    };

    abstract String getTitle();
    abstract String getFxmlFile();
    String getTitleFromResources(String key){
        LanguageResource lr = LanguageResource.getInstance();
        return lr.getResources().getString(key);
    }
}
