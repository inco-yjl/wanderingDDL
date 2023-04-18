package desktop.application.wanderingddl;

public class ToDoItem {
    private boolean checked;
    private String item;
    public ToDoItem(String item) {
        super();
        this.item=item;
        this.checked = false;
    }
    public boolean isChecked(){
        return checked;
    }
    public void setChecked() {
        this.checked = true;
    }
    public void setUnCheckd() {
        this.checked = false;
    }
    public String getText() {
        return item;
    }

    public void setText(String text) {
        item = text;
    }

}