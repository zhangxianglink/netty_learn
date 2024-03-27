package audio.v3;

import java.util.List;

/**
 * x.z
 * Create in 2023/3/14
 */
public class PyResult {

    private String method;
    private Integer segment;
    private Integer frame_offset;
    private String text;
    private List<String> tokens;
    private List<Double> timestamps;

    private boolean is_final;

    public PyResult() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getSegment() {
        return segment;
    }

    public void setSegment(Integer segment) {
        this.segment = segment;
    }

    public Integer getFrame_offset() {
        return frame_offset;
    }

    public void setFrame_offset(Integer frame_offset) {
        this.frame_offset = frame_offset;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public List<Double> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Double> timestamps) {
        this.timestamps = timestamps;
    }

    @Override
    public String toString() {
        return "PyResult{" +
                "method='" + method + '\'' +
                ", segment=" + segment +
                ", frame_offset=" + frame_offset +
                ", text='" + text + '\'' +
                ", tokens=" + tokens +
                ", timestamps=" + timestamps +
                ", is_final=" + is_final +
                '}';
    }

    public boolean isIs_final() {
        return is_final;
    }

    public void setIs_final(boolean is_final) {
        this.is_final = is_final;
    }


}
