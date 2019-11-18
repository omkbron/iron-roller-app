package mx.com.ironroller.model;

import mx.com.ironroller.model.amece71.RequestForPayment;

public class UploadedFile {
    private String originalFilename;
    private byte[] data;
    private RequestForPayment requestForPayment;

    public UploadedFile() {
    }
    
    public UploadedFile(String fileName, byte[] data) {
        this.originalFilename = fileName;
        this.data = data.clone();
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public byte[] getData() {
        if (data == null) {
            return null;
        }
        return data.clone();
    }

    public int getSize() {
        return data.length;
    }
    
    public RequestForPayment getRequestForPayment() {
        return requestForPayment;
    }
    
    public void setRequestForPayment(RequestForPayment requestForPayment) {
        this.requestForPayment = requestForPayment;
    }
}
