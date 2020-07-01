package com.example.noteai;

public class RowData {

    private long rowId;
    private String body;

    public RowData(long rowId, String body) {
        this.rowId = rowId;
        this.body = body;

    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
