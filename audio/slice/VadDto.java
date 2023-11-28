package audio.slice;

/**
 * x.z
 * Create in 2023/5/15
 */
public class VadDto {

    private String type;
    private    Double start;
    private    Double end;
    private    String filename;
    private String result ;
    private Double rtf;

    private Double time;

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "VadDto{" +
                "type='" + type + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", filename='" + filename + '\'' +
                ", result='" + result + '\'' +
                ", rtf=" + rtf +
                ", time=" + time +
                '}';
    }

    public VadDto(String type, Double start, Double end, String filename, String result, Double rtf) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.filename = filename;
        this.result = result;
        this.rtf = rtf;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getStart() {
        return start;
    }

    public void setStart(Double start) {
        this.start = start;
    }

    public Double getEnd() {
        return end;
    }

    public void setEnd(Double end) {
        this.end = end;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Double getRtf() {
        return rtf;
    }

    public void setRtf(Double rtf) {
        this.rtf = rtf;
    }
}
