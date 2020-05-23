package application;

public enum FxmlView {

    LOGIN{
        @Override
        String getTitle(){
            return ("Bookstore login");
        }
        @Override
        String getFxmlFile(){
            return "/gui/login/login.fxml";
        }
    }, USERREGISTER{
        @Override
        String getTitle(){
            return ("Registration");
        }
        @Override
        String getFxmlFile(){
            return "/gui/login/userregister/user_register.fxml";
        }
    }, ADMIN{
        @Override
        String getTitle(){
            return ("Bookstore");
        }
        @Override
        String getFxmlFile(){
            return "/gui/adminmain/admin_main.fxml";
        }
    }, ADMINCHANGEBOOK{
        @Override
        String getTitle(){
            return ("Book change");
        }
        @Override
        String getFxmlFile(){
            return "/gui/adminchangebook/change_book.fxml";
        }
    }, USER{
        @Override
        String getTitle(){
            return ("Bookstore");
        }
        @Override
        String getFxmlFile(){
            return "/gui/usermain/user_main.fxml";
        }
    }, USERCANCELCONFIRM{
        @Override
        String getTitle(){
            return ("Cancel order");
        }
        @Override
        String getFxmlFile(){
            return "/gui/usermain/confirmation/cancel_confirmation.fxml";
        }
    }, USERMAKEORDER{
        @Override
        String getTitle(){
            return ("Complete order");
        }
        @Override
        String getFxmlFile(){
            return "/gui/usermain/makeorder/make_order.fxml";
        }
    };

    abstract String getTitle();
    abstract String getFxmlFile();
}
