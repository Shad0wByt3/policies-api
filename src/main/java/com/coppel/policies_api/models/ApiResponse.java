package com.coppel.policies_api.models;

public class ApiResponse<T> {
    private Meta meta;
    private Data<T> data;

    public ApiResponse() {
    }

    public ApiResponse(Meta meta, Data<T> data) {
        this.meta = meta;
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Data<T> getData() {
        return data;
    }

    public void setData(Data<T> data) {
        this.data = data;
    }

    public static class Meta {
        private String status;
        private int statusCode;

        public Meta() {
        }

        public Meta(String status, int statusCode) {
            this.status = status;
            this.statusCode = statusCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
    }

    public static class Data<T> {
        private String mensaje;
        private T items;

        public Data() {
        }

        public Data(String mensaje, T items) {
            this.mensaje = mensaje;
            this.items = items;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public T getItems() {
            return items;
        }

        public void setItems(T items) {
            this.items = items;
        }
    }
}
