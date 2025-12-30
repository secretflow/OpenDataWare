package com.cec.examine.dict;

import java.io.Serializable;
import java.util.Objects;

public class DictBean implements Serializable {


    private String id;

    private String file;

    private String name;

    private String contents;

    private int deleted;

    @Override
    public String toString() {
        return "DictBean{" +
                "id='" + id + '\'' +
                ", file='" + file + '\'' +
                ", name='" + name + '\'' +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictBean dictBean = (DictBean) o;
        return deleted == dictBean.deleted &&
                Objects.equals(id, dictBean.id) &&
                Objects.equals(file, dictBean.file) &&
                Objects.equals(name, dictBean.name) &&
                Objects.equals(contents, dictBean.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, file, name, contents, deleted);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public DictBean(String id, String file, String name, String contents, int deleted) {
        this.id = id;
        this.file = file;
        this.name = name;
        this.contents = contents;
        this.deleted = deleted;
    }

    public DictBean() {
    }
}
