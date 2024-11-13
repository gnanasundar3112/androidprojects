package in.vinsoftsolutions.jayagrocery.Models;

public class CategoryModel {

    String Cate_id,Cate_eng_name,Cate_tam_name,Cate_image;

    public CategoryModel(String cate_id, String cate_eng_name, String cate_tam_name, String cate_image) {
        Cate_id = cate_id;
        Cate_eng_name = cate_eng_name;
        Cate_tam_name = cate_tam_name;
        Cate_image = cate_image;
    }

    public String getCate_id() {
        return Cate_id;
    }

    public void setCate_id(String cate_id) {
        Cate_id = cate_id;
    }

    public String getCate_eng_name() {
        return Cate_eng_name;
    }

    public void setCate_eng_name(String cate_eng_name) {
        Cate_eng_name = cate_eng_name;
    }

    public String getCate_tam_name() {
        return Cate_tam_name;
    }

    public void setCate_tam_name(String cate_tam_name) {
        Cate_tam_name = cate_tam_name;
    }

    public String getCate_image() {
        return Cate_image;
    }

    public void setCate_image(String cate_image) {
        Cate_image = cate_image;
    }
}
