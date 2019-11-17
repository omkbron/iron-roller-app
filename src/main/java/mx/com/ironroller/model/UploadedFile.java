package mx.com.ironroller.model;

public class UploadedFile {
    private String originalFilename;
    private byte[] data;

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
}
