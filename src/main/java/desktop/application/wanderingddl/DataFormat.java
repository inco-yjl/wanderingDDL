package desktop.application.wanderingddl;

public class DataFormat {
    int id;
    String content;
    public DataFormat(int id,String content){
        this.id=id;
        this.content=content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
